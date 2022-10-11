package com.hau.ketnguyen.it.service;

import com.hau.ketnguyen.it.entity.auth.UserVerification;

import java.util.Optional;

public interface UserVerificationService {
    Optional<UserVerification> findByCode(String code);
    Optional<UserVerification> findByUserId(Integer userId);
    void save(Integer userId, String code);
}
