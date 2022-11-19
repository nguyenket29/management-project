package com.hau.ketnguyen.it.model.request.auth;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}
