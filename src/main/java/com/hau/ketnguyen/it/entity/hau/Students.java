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
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "topic_id")
    private Long topicId;

    @Column(name = "class_id")
    private Long classId;
}
