package com.hau.ketnguyen.it.model.dto.auth;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String confirmPassword;
    private Short status;
    private String email;
    private String type;
    private UserInfoDTO userInfoDTO;
    private List<String> listRole;
}
