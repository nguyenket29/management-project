package com.hau.ketnguyen.it.entity.hau;

import com.hau.ketnguyen.it.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table
@Entity(name = "comments")
@Data
public class Comments extends BaseEntity {
    @Column(name = "content")
    private String content;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "topic_id")
    private Long topicId;
}
