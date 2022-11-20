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
    private String firstName;
    private String password;
    private String confirmPassword;
    private String lastName;
    private String address;
    private Short status;
    private String email;
    private String avatar;
    private Date birthday;
    private Short gender;
    private String phoneNumber;
    private List<String> listRole;
}
