package com.hau.ketnguyen.it.entity.auth;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "refresh_token")
@Data
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    private Integer userId;

    @Column(name = "access_token", nullable = false, unique = true)
    private String accessToken;

    @Column(name = "refresh_token", nullable = false, unique = true)
    private String refreshToken;

    @Column(nullable = false, name = "expiry_date")
    private Date expiryDate;

    @Column(name = "status")
    private boolean status;
}
