package com.hau.ketnguyen.it.entity.auth;

import com.hau.ketnguyen.it.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reset_password_token")
@Data
public class ResetPasswordToken extends BaseEntity {
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "expiry_date")
    private Date expiryDate;
}
