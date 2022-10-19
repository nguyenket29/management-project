package com.hau.ketnguyen.it.service.impl;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.AuthorityUtil;
import com.hau.ketnguyen.it.common.util.JwtTokenUtil;
import com.hau.ketnguyen.it.config.auth.Commons;
import com.hau.ketnguyen.it.entity.auth.*;
import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import com.hau.ketnguyen.it.model.request.SignupRequest;
import com.hau.ketnguyen.it.model.request.TokenRefreshRequest;
import com.hau.ketnguyen.it.model.response.TokenRefreshResponse;
import com.hau.ketnguyen.it.model.response.UserResponse;
import com.hau.ketnguyen.it.repository.RefreshTokenReps;
import com.hau.ketnguyen.it.repository.RoleReps;
import com.hau.ketnguyen.it.repository.UserReps;
import com.hau.ketnguyen.it.repository.UserVerificationReps;
import com.hau.ketnguyen.it.service.*;
import com.hau.ketnguyen.it.service.mapper.RefreshTokenMapper;
import com.hau.ketnguyen.it.service.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.*;

import static com.hau.ketnguyen.it.common.enums.RoleEnums.Role.USER;

@Service
@Slf4j
@AllArgsConstructor
public class AuthServiceimpl implements AuthService {
    private final UserReps userReps;
    private final RefreshTokenService refreshToken;
    private final UserMapper userMapper;
    private final JwtTokenUtil tokenUtil;
    private final RefreshTokenReps refreshTokenReps;
    private final RefreshTokenMapper refreshTokenMapper;
    private final UserVerificationService userVerificationService;
    private final EmailService emailService;
    private final UserVerificationReps userVerificationReps;
    private final RoleReps roleReps;
    private final UserService userService;

    /**
     * Lấy lại access token khi token hết hạn
     * @RequestBody: request
     * @Return: TokenRefreshResponse
     * */
    @Override
    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        RefreshToken refresh = refreshTokenReps.findByRefreshToken(requestRefreshToken)
                .orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Refresh token not found."));
        User user = userReps.findById(refresh.getUserId())
                .orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Username not found."));

        Set<GrantedAuthority> authorities = new HashSet<>();
        Set<String> roles = new HashSet<>();
        user.getRoles().forEach(r -> roles.add(r.getCode()));
        roles.forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r)));

        CustomUser customUser = new CustomUser(user.getUsername(), user.getPassword(), true,
                true, true, true, authorities);
        customUser.setId(user.getId());
        customUser.setFullName(user.getFullName());
        customUser.setAvatar(user.getAvatar());

        String accessToken = tokenUtil.generateToken(customUser.getUsername(), roles,
                customUser.getId(), customUser.getFullName(), customUser.getAvatar());

        return refreshToken.findByRefreshToken(requestRefreshToken)
                .map(refreshToken::verifyExpiration).map(u -> {
                    if (!tokenUtil.validateJwtToken(u.getAccessToken())) {
                        u.setAccessToken(accessToken);
                        u.setStatus(true);
                        refreshTokenMapper.copy(u, refresh);
                        refreshTokenReps.save(refresh);
                        return new TokenRefreshResponse(accessToken, u.getRefreshToken());
                    }
                    return new TokenRefreshResponse(u.getAccessToken(), u.getRefreshToken());
                }).orElseThrow(() -> APIException.from(HttpStatus.FORBIDDEN).withMessage("Refresh token is not in database!"));
    }

    /**
     * Lấy thông tin tài khoản
     * @Return: UserResponse
     * */
    @Override
    public UserResponse getInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        Optional<UserDTO> userDTO = userService.findByUsernameAndStatus(customUser.getUsername(),
                User.Status.ACTIVE).map(userMapper::to);
        if (userDTO.isPresent()) {
            Set<String> roles = AuthorityUtil.authorityListToSet(customUser.getAuthorities());
            return new UserResponse(userDTO.get().getId(), userDTO.get().getUsername(),
                    userDTO.get().getFirstName(), userDTO.get().getLastName(),
                    userDTO.get().getAddress(), userDTO.get().getStatus(), userDTO.get().getAvatar(),
                    userDTO.get().getBirthday(), userDTO.get().getGender(), roles);
        }
        return null;
    }

    /**
     * Đăng xuất
     *
     * @Param: request
     * @Param: response
     * @Return: void
     * */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String accessToken = parseJwt(request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextLogoutHandler contextLogoutHandler = new SecurityContextLogoutHandler();
        if (authentication != null) {
            if (authentication.isAuthenticated()) {
                CustomUser user = (CustomUser) authentication.getPrincipal();
                contextLogoutHandler.logout(request, response, authentication);
                if (contextLogoutHandler.isInvalidateHttpSession()) {
                    authentication.setAuthenticated(false);
                    Optional<RefreshToken> refreshTokens =
                            refreshTokenReps.findByUserIdAndAccessToken(user.getId(), accessToken);
                    if (refreshTokens.isPresent()) {
                        refreshTokenReps.delete(refreshTokens.get());
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            }
        }
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(Commons.AUTH_HEADER);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }

    /**
     * Đăng kí
     *
     * @RequestBody: signupRequest
     * @Param: request
     * @Return: UserDTO
     * */
    @Override
    public UserDTO signup(SignupRequest signupRequest, HttpServletRequest request) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        validRegister(signupRequest);

        //disable new user before activation
        User user = new User();
        user.setStatus(User.Status.WAITING);
        user.setUsername(signupRequest.getUsername());
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        //set role user for all account when register
        Set<Role> roles = new HashSet<>();
        Optional<Role> role = roleReps.findByCode(USER.name());
        role.ifPresent(roles::add);
        user.setRoles(roles);

        Optional<UserDTO> userDTO = Optional.of(userMapper.to(userReps.save(user)));
        //create and save verification code if the user is saved
        userDTO.ifPresent(u -> {
            try {
                String code = UUID.randomUUID().toString();
                userVerificationService.save(userDTO.get().getId(), code);

                //send verify to email
                emailService.sendMail(u, request);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw APIException.from(HttpStatus.BAD_REQUEST).withMessage(e.getMessage());
            }
        });

        return userDTO.get();
    }


    /**
     * Valid register account
     * @RequestBody: SignupRequest
     * */
    private void validRegister(SignupRequest signupRequest) {
        if (userReps.existsByUsername(signupRequest.getUsername())) {
            throw APIException.from(HttpStatus.BAD_REQUEST).withMessage("Username is existed.");
        }

        if (userReps.existsByEmail(signupRequest.getEmail())) {
            throw APIException.from(HttpStatus.BAD_REQUEST).withMessage("Email is existed.");
        }

        if (Objects.nonNull(signupRequest.getPassword())
                && Objects.nonNull(signupRequest.getConfirmPassword())) {
            if (!Objects.equals(signupRequest.getPassword(), signupRequest.getConfirmPassword())) {
                throw APIException.from(HttpStatus.BAD_REQUEST).withMessage("Password must match.");
            }
        }
    }

    /**
     * xác nhận tài khoản
     *
     * @Param: code
     * @Return: boolean
     * */
    @Override
    public boolean verifyAccount(String code) {
        Optional<UserVerification> userVerification = userVerificationService.findByCode(code);
        if (userVerification.isPresent()) {
            UserVerification verification = userVerification.get();
            if (!verification.getStatus().equals(UserVerification.Status.ACTIVE)) {
                verification.setStatus(UserVerification.Status.ACTIVE);
                verification.setType(UserVerification.Type.email);
                Optional<User> user = userReps.findById(verification.getUserId());
                if (user.isPresent()) {
                    //if the user account is not actived
                    if (!user.get().getStatus().equals(User.Status.ACTIVE)) {
                        //get time current
                        Date date = Date.from(Instant.now());
                        //check if the code is expired
                        if (userVerification.get().getExpireDate().before(date)) {
                            throw APIException.from(HttpStatus.BAD_REQUEST)
                                    .withMessage("Your verification code has expired");
                        } else {
                            //the code is valid
                            //active the user account
                            user.get().setStatus(User.Status.ACTIVE);
                            userReps.save(user.get());
                            return true;
                        }
                    } else {
                        return false;
                    }
                }
                userVerificationReps.save(verification);
            }
        } else {
            throw APIException.from(HttpStatus.BAD_REQUEST)
                    .withMessage("Your verification code is invalid.");
        }
        return false;
    }
}
