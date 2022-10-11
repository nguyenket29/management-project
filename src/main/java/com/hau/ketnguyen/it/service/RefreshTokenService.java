package com.hau.ketnguyen.it.service;

import com.hau.ketnguyen.it.model.dto.auth.RefreshTokenDTO;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshTokenDTO> findByRefreshToken(String token);
    Optional<RefreshTokenDTO> findByUserId(Integer id);
    RefreshTokenDTO createRefreshToken(Integer userId, String accessToken);
    RefreshTokenDTO verifyExpiration(RefreshTokenDTO token);
}
