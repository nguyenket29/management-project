package com.hau.ketnguyen.it.model.dto.auth;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
@Builder
public class UserInfoDTO {
    private Long id;
    private String createdBy;
    private Date created;
    private String updatedBy;
    private Date updated;
    private String fullName;
    private String avatar;
    private Instant dateOfBirth;
    private String town;
    private short gender;
    private String address;
    private String phoneNumber;
}
