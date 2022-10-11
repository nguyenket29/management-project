package com.hau.ketnguyen.it.service.impl;

import com.hau.ketnguyen.it.entity.auth.RefreshToken;
import com.hau.ketnguyen.it.model.dto.auth.RefreshTokenDTO;
import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import com.hau.ketnguyen.it.repository.RefreshTokenReps;
import com.hau.ketnguyen.it.repository.UserReps;
import com.hau.ketnguyen.it.service.RefreshTokenService;
import com.hau.ketnguyen.it.service.mapper.RefreshTokenMapper;
import com.hau.ketnguyen.it.service.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final long refreshExpiration = 600000;
    private final RefreshTokenReps tokenReps;
    private final UserReps userReps;
    private final UserMapper userMapper;
    private final RefreshTokenMapper refreshTokenMapper;

    @Override
    public Optional<RefreshTokenDTO> findByRefreshToken(String token) {
        return tokenReps.findByRefreshToken(token).map(refreshTokenMapper::to);
    }

    @Override
    public Optional<RefreshTokenDTO> findByUserId(Integer id) {
        return tokenReps.findByUserId(id).map(refreshTokenMapper::to);
    }

    @Override
    public RefreshTokenDTO createRefreshToken(Integer userId, String accessToken) {
        Optional<UserDTO> userDTO = userReps.findById(userId).map(userMapper::to);
        if (userDTO.isPresent()) {
            RefreshTokenDTO dto = new RefreshTokenDTO();
            dto.setUserId(userDTO.get().getId());
            dto.setExpiryDate(Date.from(Instant.now().plusMillis(refreshExpiration)));
            dto.setRefreshToken(UUID.randomUUID().toString());
            dto.setAccessToken(accessToken);
            return refreshTokenMapper.to(tokenReps.save(refreshTokenMapper.from(dto)));
        }
        return null;
    }

    @Override
    public RefreshTokenDTO verifyExpiration(RefreshTokenDTO token) {
        Optional<RefreshToken> tokenRfs = tokenReps.findByRefreshToken(token.getRefreshToken());
        if (tokenRfs.isPresent()) {
            RefreshToken tokenEntity = tokenRfs.get();
            if (token.getExpiryDate().compareTo(Date.from(Instant.now())) < 0) {
                token.setExpiryDate(Date.from(Instant.now().plusMillis(refreshExpiration)));
                refreshTokenMapper.copy(token, tokenEntity);
                tokenReps.save(tokenEntity);
            }
        }
        return token;
    }
}
