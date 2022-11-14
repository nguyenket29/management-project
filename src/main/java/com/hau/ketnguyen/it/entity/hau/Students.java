package com.hau.ketnguyen.it.entity.hau;

import com.hau.ketnguyen.it.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Table
@Entity(name = "students")
@Data
public class Students extends BaseEntity {
    @Column(name = "name")
    private String name;

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

    @Column(name = "topic_id")
    private Long topicId;

    @Column(name = "class_id")
    private Long classId;
}
