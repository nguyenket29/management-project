package com.hau.ketnguyen.it.entity.hau;

import com.hau.ketnguyen.it.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table
@Entity(name = "classes")
@Data
public class Classes extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "student_number")
    private Integer stdNumber;

    @Column(name = "faculty_id")
    private Long facultyId;
}
