package com.hau.ketnguyen.it.entity.hau;

import com.hau.ketnguyen.it.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Table
@Entity(name = "topics")
@Data
public class Topics extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "student_number")
    private Integer stdNumber;

    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "src_code_id")
    private Long srcCodeId;

    @Column(name = "year")
    private Date year;

    @Column(name = "score")
    private Float score;

    @Column(name = "lecturer_id")
    private Long lecturerId;
}
