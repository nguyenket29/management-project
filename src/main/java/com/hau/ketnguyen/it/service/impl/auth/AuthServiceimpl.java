package com.hau.ketnguyen.it.service.impl.auth;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.AuthorityUtil;
import com.hau.ketnguyen.it.common.util.BeanUtil;
import com.hau.ketnguyen.it.common.util.JwtTokenUtil;
import com.hau.ketnguyen.it.config.auth.Commons;
import com.hau.ketnguyen.it.entity.auth.*;
import com.hau.ketnguyen.it.entity.hau.Lecturers;
import com.hau.ketnguyen.it.entity.hau.Students;
import com.hau.ketnguyen.it.entity.hau.UserInfo;
import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import com.hau.ketnguyen.it.model.request.auth.SignupRequest;
import com.hau.ketnguyen.it.model.request.auth.TokenRefreshRequest;
import com.hau.ketnguyen.it.model.request.auth.UserInfoRequest;
import com.hau.ketnguyen.it.model.response.TokenRefreshResponse;
import com.hau.ketnguyen.it.model.response.UserResponse;
import com.hau.ketnguyen.it.repository.auth.*;
import com.hau.ketnguyen.it.repository.hau.LecturerReps;
import com.hau.ketnguyen.it.repository.hau.StudentReps;
import com.hau.ketnguyen.it.service.*;
import com.hau.ketnguyen.it.service.mapper.RefreshTokenMapper;
import com.hau.ketnguyen.it.service.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.*;

import static com.hau.ketnguyen.it.common.enums.RoleEnums.Role.USER;
import static com.hau.ketnguyen.it.common.enums.TypeUser.LECTURE;
import static com.hau.ketnguyen.it.common.enums.TypeUser.STUDENT;

@Service
@Slf4j
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
    private final UserInfoReps userInfoReps;
    private final ResetPasswordTokenService resetPasswordTokenService;
    private final StudentReps studentReps;
    private final LecturerReps lecturerReps;
    private final String portFe;

    public AuthServiceimpl(UserReps userReps, RefreshTokenService refreshToken, UserMapper userMapper,
                           JwtTokenUtil tokenUtil, RefreshTokenReps refreshTokenReps,
                           RefreshTokenMapper refreshTokenMapper, UserVerificationService userVerificationService,
                           EmailService emailService, UserVerificationReps userVerificationReps, RoleReps roleReps, UserService userService,
                           UserInfoReps userInfoReps, ResetPasswordTokenService resetPasswordTokenService,
                           StudentReps studentReps, LecturerReps lecturerReps, @Value("${port-fe}") String portFe) {
        this.userReps = userReps;
        this.refreshToken = refreshToken;
        this.userMapper = userMapper;
        this.tokenUtil = tokenUtil;
        this.refreshTokenReps = refreshTokenReps;
        this.refreshTokenMapper = refreshTokenMapper;
        this.userVerificationService = userVerificationService;
        this.emailService = emailService;
        this.userVerificationReps = userVerificationReps;
        this.roleReps = roleReps;
        this.userService = userService;
        this.userInfoReps = userInfoReps;
        this.resetPasswordTokenService = resetPasswordTokenService;
        this.studentReps = studentReps;
        this.lecturerReps = lecturerReps;
        this.portFe = portFe;
    }

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
        UserInfo userInfo = setUserInfo(user);

        Set<GrantedAuthority> authorities = new HashSet<>();
        Set<String> roles = new HashSet<>();
        user.getRoles().forEach(r -> roles.add(r.getCode()));
        roles.forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r)));

        CustomUser customUser = new CustomUser(user.getUsername(), user.getPassword(), true,
                true, true, true, authorities);
        customUser.setId(user.getId());
        customUser.setFullName(userInfo.getFullName());
        customUser.setAvatar(userInfo.getAvatar());

        String accessToken = tokenUtil.generateToken(customUser.getUsername(), roles,
                customUser.getId(), customUser.getFullName(), customUser.getAvatar(), customUser.getType());

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

    public UserInfo setUserInfo(User userEntity) {
        UserInfo userInfo = new UserInfo();
        if (userEntity.getType().equalsIgnoreCase(STUDENT.name())) {
            Optional<Students> studentOptional = studentReps.findByUserId(userEntity.getId());

            if (studentOptional.isEmpty()) {
                throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy sinh viên");
            }

            userInfo = userInfoReps.findById(studentOptional.get().getUserInfoId())
                    .orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy thông tin sinh viên"));
        } else if (userEntity.getType().equalsIgnoreCase(LECTURE.name())) {
            Optional<Lecturers> lecturersOptional = lecturerReps.findByUserId(userEntity.getId());

            if (lecturersOptional.isEmpty()) {
                throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy giảng viên");
            }

            userInfo = userInfoReps.findById(lecturersOptional.get().getUserInfoId())
                    .orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy thông tin giảng viên"));
        }
        return userInfo;
    }

    /**
     * Lấy thông tin tài khoản
     * @Return: UserResponse
     * */
    @Override
    public UserResponse getInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        Optional<User> userOpt = userReps.findById(customUser.getId());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            UserInfo userInfo = setUserInfo(user);

            Set<String> roles = AuthorityUtil.authorityListToSet(customUser.getAuthorities());
            return UserResponse.builder()
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .status(user.getMapStatus().get(user.getStatus()))
                    .type(user.getType())
                    .address(userInfo.getAddress())
                    .avatar(userInfo.getAvatar())
                    .town(userInfo.getTown())
                    .authorities(roles)
                    .fullName(userInfo.getFullName())
                    .dateOfBirth(userInfo.getDateOfBirth())
                    .gender(user.getMapGender().get(userInfo.getGender()))
                    .phoneNumber(userInfo.getPhoneNumber())
                    .build();
        }
        return null;
    }

    @Override
    public UserResponse editUserInfo(UserInfoRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        Optional<User> userOpt = userReps.findById(customUser.getId());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            UserInfo userInfo = setUserInfo(user);
            BeanUtil.copyNonNullProperties(request, userInfo);
            userInfoReps.save(userInfo);

            Set<String> roles = AuthorityUtil.authorityListToSet(customUser.getAuthorities());
            return UserResponse.builder()
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .status(user.getMapStatus().get(user.getStatus()))
                    .type(user.getType())
                    .address(userInfo.getAddress())
                    .avatar(userInfo.getAvatar())
                    .town(userInfo.getTown())
                    .authorities(roles)
                    .fullName(userInfo.getFullName())
                    .dateOfBirth(userInfo.getDateOfBirth())
                    .gender(user.getMapGender().get(userInfo.getGender()))
                    .phoneNumber(userInfo.getPhoneNumber())
                    .build();
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
        user.setEmail(signupRequest.getEmail());

        if (signupRequest.getPassword() != null && signupRequest.getConfirmPassword() != null
                && Objects.equals(signupRequest.getPassword(), signupRequest.getConfirmPassword())) {
            user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        }

        //set role user for all account when register
        Set<Role> roles = new HashSet<>();
        Optional<Role> role = roleReps.findByCode(USER.name());
        role.ifPresent(roles::add);
        user.setRoles(roles);

        Optional<UserDTO> userDTO = Optional.of(userMapper.to(userReps.save(user)));
        //create and save verification code if the user is saved
        userDTO.ifPresent(u -> {
            try {
                UserInfo userInfo = new UserInfo();
                String code = UUID.randomUUID().toString();
                userVerificationService.save(userDTO.get().getId(), code);
                userInfoReps.save(userInfo);

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

    @Override
    public void forgotPassword(String email, HttpServletRequest request) throws MessagingException {
        User user = userReps.findByEmail(email).orElseThrow(() ->
                APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy người dùng với email: " + email));
        UserDTO userDto = userMapper.to(user);
        UserInfo userInfo = setUserInfo(user);

        String token = UUID.randomUUID().toString();
        if (resetPasswordTokenService.createTokenResetPassword(userDto.getId(), token)) {
            String url = ServletUriComponentsBuilder.fromRequestUri(request)
                    .replacePath(request.getPathInfo())
                    .build()
                    .toUriString();

            Map<String, Object> variables = new HashMap<>();
            String sub = "Email Reset Password !";
            String fullName = userInfo.getFullName();
            String userName = userDto.getUsername();

            String activeUrl = splitUrl(url).concat(portFe).concat("/#/pages/update-password?token=").concat(token);
            variables.put("url", activeUrl);
            variables.put("fullName", fullName);
            variables.put("userName", userName);
            emailService.sendEmail(sub, variables, null, null, userDto, "mail/forgot-password");
        }
    }

    private String splitUrl(String url) {
        return url.replaceAll("[0-9]", "");
    }

    @Async
    @Override
    public boolean updatePassword(String newPassword, String token, String oldPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (token != null) {
            String rs = resetPasswordTokenService.validatePasswordResetToken(token);

            if (rs != null) {
                throw APIException.from(HttpStatus.BAD_REQUEST).withMessage(rs);
            }

            Optional<ResetPasswordToken> resetPasswordToken = resetPasswordTokenService.findByToken(token);
            if (resetPasswordToken.isPresent()) {
                Optional<User> userOptional = userReps.findById(resetPasswordToken.get().getUserId());
                if (userOptional.isPresent()) {
                    if (newPassword.equals(oldPassword)) {
                        userOptional.get().setPassword(passwordEncoder.encode(newPassword));
                    } else {
                        throw APIException.from(HttpStatus.BAD_REQUEST).withMessage("Mật khẩu không trùng khớp !");
                    }

                    User user = userReps.save(userOptional.get());
                    return user.getId() > 0;
                }
            }
        }
        return false;
    }
}
