package com.hau.ketnguyen.it.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.AuthorityUtil;
import com.hau.ketnguyen.it.common.util.JwtTokenUtil;
import com.hau.ketnguyen.it.entity.auth.CustomUser;
import com.hau.ketnguyen.it.entity.auth.User;
import com.hau.ketnguyen.it.entity.hau.Lecturers;
import com.hau.ketnguyen.it.entity.hau.Students;
import com.hau.ketnguyen.it.entity.hau.UserInfo;
import com.hau.ketnguyen.it.model.dto.auth.RefreshTokenDTO;
import com.hau.ketnguyen.it.model.request.auth.LoginRequest;
import com.hau.ketnguyen.it.model.response.JwtResponse;
import com.hau.ketnguyen.it.repository.auth.UserInfoReps;
import com.hau.ketnguyen.it.repository.auth.UserReps;
import com.hau.ketnguyen.it.service.RefreshTokenService;
import com.hau.ketnguyen.it.service.impl.auth.AuthServiceimpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.hau.ketnguyen.it.common.enums.TypeUser.LECTURE;
import static com.hau.ketnguyen.it.common.enums.TypeUser.STUDENT;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthServiceimpl authServiceimpl;
    private final JwtTokenUtil tokenUtil;
    private final UserInfoReps userInfoReps;
    private final UserReps userReps;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, BCryptPasswordEncoder bCryptPasswordEncoder,
                                   JwtTokenUtil tokenUtil, RefreshTokenService refreshTokenService, AuthServiceimpl authServiceimpl, UserInfoReps userInfoReps, UserReps userReps) {
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenUtil = tokenUtil;
        this.authServiceimpl = authServiceimpl;
        this.userInfoReps = userInfoReps;
        this.userReps = userReps;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
            Authentication authentication = null;
            try {
                authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                                loginRequest.getPassword()));
            } catch (BadCredentialsException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad credentials!");
            } catch (InternalAuthenticationServiceException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal authentication!");
            } catch (UsernameNotFoundException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not found!");
            }
            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        CustomUser user = (CustomUser) authResult.getPrincipal();
        User userEntity = userReps.findByUsernameAndStatus(user.getUsername(), User.Status.ACTIVE)
                .orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Username not found"));

        Set<String> authorities = new HashSet<>();
        AuthorityUtil.authorityListToSet(user.getAuthorities()).forEach(au -> {
            authorities.add("ROLE_".concat(au));
        });

        UserInfo userInfo = authServiceimpl.setUserInfo(userEntity);
        user.setFullName(userInfo.getFullName());
        user.setAvatar(userInfo.getAvatar());

        Optional<RefreshTokenDTO> refreshToken = refreshTokenService.findByUserId(user.getId());
        JwtResponse tokenResponse = null;
        String token = tokenUtil.generateToken(user.getUsername(), authorities,
                user.getId(), user.getFullName(), user.getAvatar(), user.getType());
        if (refreshToken.isPresent()) {
            refreshToken.get().setAccessToken(token);
            RefreshTokenDTO tokenDTO = refreshTokenService.update(refreshToken.get());
            tokenResponse = setJwtResponse(tokenDTO.getAccessToken(), tokenDTO.getRefreshToken(), tokenDTO.getExpiryDate());
        } else {
            RefreshTokenDTO refreshTokenDTO = refreshTokenService.createRefreshToken(user.getId(), token);
            tokenResponse = setJwtResponse(token, refreshTokenDTO.getRefreshToken(), refreshTokenDTO.getExpiryDate());
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader(Commons.AUTH_HEADER, token);
        response.setHeader(Commons.Refresh_Token, tokenResponse.getRefreshToken());
        response.getWriter().println(new ObjectMapper().writer().writeValueAsString(tokenResponse));
    }

    private JwtResponse setJwtResponse(String accToken, String rfToken, Date expireDate) {
        JwtResponse tokenResponse = new JwtResponse();
        tokenResponse.setToken(accToken);
        tokenResponse.setRefreshToken(rfToken);
        tokenResponse.setRefreshTokenExpiredDate(expireDate);
        return tokenResponse;
    }
}
