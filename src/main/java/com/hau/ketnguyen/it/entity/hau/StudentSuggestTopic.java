package com.hau.ketnguyen.it.entity.hau;

import com.hau.ketnguyen.it.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table
@Entity(name = "student_suggest_topics")
@Data
public class StudentSuggestTopic extends BaseEntity {
    @Column(name = "status_suggest", columnDefinition = "boolean default false")
    private Boolean statusSuggest = false;

    @Column(name = "status_approve", columnDefinition = "boolean default false")
    private Boolean statusApprove = false;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "topic_id")
    private Long topicId;
}
