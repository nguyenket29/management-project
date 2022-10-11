package com.hau.ketnguyen.it.model.request;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}
