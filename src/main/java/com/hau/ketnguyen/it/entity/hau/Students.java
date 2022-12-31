package com.hau.ketnguyen.it.entity.hau;

import com.hau.ketnguyen.it.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table
@Entity(name = "students")
@Data
public class Students extends BaseEntity {
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "student_pas", columnDefinition = "boolean default false")
    private boolean stdPass;

    @Column(name = "code_student")
    private String codeStudent;

    @Column(name = "topic_id")
    private Long topicId;

    @Column(name = "class_id")
    private Long classId;

    @Column(name = "user_info_id")
    private Long userInfoId;
}
