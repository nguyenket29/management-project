package com.hau.ketnguyen.it.service;

import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import com.hau.ketnguyen.it.model.request.SignupRequest;
import com.hau.ketnguyen.it.model.request.TokenRefreshRequest;
import com.hau.ketnguyen.it.model.response.TokenRefreshResponse;
import com.hau.ketnguyen.it.model.response.UserResponse;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {
    TokenRefreshResponse refreshToken(TokenRefreshRequest request);
    UserResponse getInfo();
    void logout(HttpServletRequest request, HttpServletResponse response);
    UserDTO signup(SignupRequest signupRequest, HttpServletRequest request);
    boolean verifyAccount(String code);
    void forgotPassword(String email, HttpServletRequest request) throws MessagingException;

    boolean updatePassword(String newPassword, String token, String oldPassword);
}
