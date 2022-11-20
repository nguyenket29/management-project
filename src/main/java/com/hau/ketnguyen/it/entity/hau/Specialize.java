package com.hau.ketnguyen.it.entity.hau;

import com.hau.ketnguyen.it.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table
@Entity(name = "specialize")
@Data
public class Specialize extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "faculty_id")
    private Long facultyId;

    @Column(name = "training_system_id")
    private Long trainSystemId;
}
