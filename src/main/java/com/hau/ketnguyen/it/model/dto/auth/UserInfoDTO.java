package com.hau.ketnguyen.it.model.dto.auth;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UserInfoDTO {
    private String fullName;
    private String avatar;
    private Instant dateOfBirth;
    private String town;
    private short gender;
    private String marriageStatus;
    private Integer userId;
    private String address;
    private String phoneNumber;
}
