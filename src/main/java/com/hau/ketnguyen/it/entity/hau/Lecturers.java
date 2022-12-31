package com.hau.ketnguyen.it.entity.hau;

import com.hau.ketnguyen.it.entity.BaseEntity;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Table
@Entity(name = "lecturers")
@Data
public class Lecturers extends BaseEntity {
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "code_lecture")
    private String codeLecture;

    //chức vụ
    @Column(name = "regency")
    private String regency;

    //cấp bậc
    @Column(name = "degree")
    private String degree;

    @Column(name = "faculty_id")
    private Long facultyId;

    @Column(name = "user_info_id")
    private Long userInfoId;
}
