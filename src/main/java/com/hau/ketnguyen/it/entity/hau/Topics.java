package com.hau.ketnguyen.it.entity.hau;

import com.hau.ketnguyen.it.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
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
    private String fileId;

    @Column(name = "year")
    private Date year;

    @Column(name = "description")
    @Lob
    private String description;

    @Column(name = "status", columnDefinition = "boolean default false")
    private Boolean status = false;

    @Column(name = "score_guide", columnDefinition = "float default 0")
    private Float scoreGuide;

    @Column(name = "score_counter_argument", columnDefinition = "float default 0")
    private Float scoreCounterArgument;

    @Column(name = "score_process_one", columnDefinition = "float default 0")
    private Float scoreProcessOne;

    @Column(name = "score_process_two", columnDefinition = "float default 0")
    private Float scoreProcessTwo;

    @Column(name = "lecturer_guide_id")
    private Long lecturerGuideId;

    @Column(name = "lecturer_counter_argument_id")
    private Long lecturerCounterArgumentId;

    @Column(name = "category_id")
    private Long categoryId;
}
