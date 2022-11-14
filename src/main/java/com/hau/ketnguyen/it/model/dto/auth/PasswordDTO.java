package com.hau.ketnguyen.it.model.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordDTO {
    private String oldPassword;

    private  String token;

    private String newPassword;
}
