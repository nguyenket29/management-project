package com.hau.ketnguyen.it.entity.hau;

import com.hau.ketnguyen.it.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Table
@Entity(name = "lecturers")
@Data
public class Lecturers extends BaseEntity {
    public static final class Gender {
        public static final short MALE = 0;
        public static final short FEMALE = 1;
        public static final short OTHER = 2;
    }

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "gender")
    private short gender;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    //chức vụ
    @Column(name = "regency")
    private String regency;

    //cấp bậc
    @Column(name = "degree")
    private String degree;

    @Column(name = "faculty_id")
    private Long facultyId;

    @Column(name = "workplace_id")
    private Long workplaceId;
}
