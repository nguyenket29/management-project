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
    @Column(name = "lecturer_ids")
    private String lecturerIds;

    @Column(name = "topic_id")
    private Long topicId;

    @Column(name = "topic_ids")
    private String topicIds;

    @Column(name = "score", columnDefinition = "float default 0")
    private Float score;

    @Column(name = "name_assembly")
    private String nameAssembly;

    @Column(name = "lecture_resident_id")
    private Long lecturePresidentId;

    @Column(name = "lecture_secretary_id")
    private Long lectureSecretaryId;
}
