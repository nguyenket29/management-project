package com.hau.ketnguyen.it.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min = 10, max = 40)
    private String email;

    @NotNull
    private String firstName;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    @Size(min = 6, max = 40)
    private String confirmPassword;

    @NotNull
    private String lastName;

    private Date birthday;
    private Short status;
    private Short gender;
    private String address;
}
