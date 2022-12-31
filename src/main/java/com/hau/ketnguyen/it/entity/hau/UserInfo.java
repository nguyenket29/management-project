package com.hau.ketnguyen.it.entity.hau;

import com.hau.ketnguyen.it.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.Instant;

@Data
@Entity
@Table(name = "user_info")
public class UserInfo extends BaseEntity {
    public static final class Gender {
        public static final short MALE = 0;
        public static final short FEMALE = 1;
        public static final short OTHER = 2;
    }

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "date_of_birth")
    private Instant dateOfBirth;

    @Column(name = "town")
    private String town;

    @Column(name = "gender")
    private short gender;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;
}
