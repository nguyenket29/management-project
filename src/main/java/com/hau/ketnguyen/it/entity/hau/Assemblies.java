package com.hau.ketnguyen.it.entity.hau;

import com.hau.ketnguyen.it.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table
@Entity(name = "assembly")
@Data
public class Assemblies extends BaseEntity {
    @Column(name = "lecturer_id")
    private Long lecturerId;

    @Column(name = "topic_id")
    private Long topicId;
}
