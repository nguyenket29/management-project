package com.hau.ketnguyen.it.model.request.auth;

import lombok.Data;

import java.time.Instant;

@Data
public class UserInfoRequest {
    private String fullName;
    private String avatar;
    private Instant dateOfBirth;
    private String town;
    private short gender;
    private String address;
    private String phoneNumber;
}
