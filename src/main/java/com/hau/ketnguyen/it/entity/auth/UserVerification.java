package com.hau.ketnguyen.it.entity.auth;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "user_verification")
public class UserVerification {
    public static final class Status {
        public static final String UN_ACTIVE = "WAITING_VERIFY";
        public static final String ACTIVE = "VERIFIED";
    }

    public static final class Type {
        public static final String message = "msg";
        public static final String email = "email";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "expire_date")
    private Date expireDate;

    @Column(name = "status")
    private String status;

    @Column(name = "type")
    private String type;
}
