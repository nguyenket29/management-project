package com.hau.ketnguyen.it.service;

import com.hau.ketnguyen.it.entity.auth.ResetPasswordToken;

import java.util.Optional;

public interface ResetPasswordTokenService {
    boolean createTokenResetPassword(Integer userId, String  token);
    String validatePasswordResetToken(String token);

    Optional<ResetPasswordToken> findByToken(String token);
}
