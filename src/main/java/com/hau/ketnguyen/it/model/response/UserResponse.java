package com.hau.ketnguyen.it.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.Set;

@Data
@Builder
public class UserResponse {
    public static final class Status {
        public static final short ACTIVE = 1;
        public static final short WAITING = 0;
        public static final short LOCK = -1;
    }

    public static final class Gender {
        public static final short MALE = 0;
        public static final short FEMALE = 1;
        public static final short OTHER = 2;
    }

    private Integer id;
    private String username;
    private String email;
    private String fullName;
    private String avatar;
    private Instant dateOfBirth;
    private String town;
    private Short gender;
    private String marriageStatus;
    private String address;
    private String phoneNumber;
    private Short status;
    private String type;
    private Set<String> authorities;
}
