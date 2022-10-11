package com.hau.ketnguyen.it.model.dto.auth;

import lombok.Data;

import java.util.Date;

@Data
public class RefreshTokenDTO {
    private Long id;
    private Integer userId;
    private String refreshToken;
    private String accessToken;
    private boolean status;
    private Date expiryDate;
}
