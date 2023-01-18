package com.hau.ketnguyen.it.model.dto.hau;

import lombok.Data;

import java.util.Date;

@Data
public class StudentTopicDTO {
    private Long id;
    private String createdBy;
    private Date created;
    private String updatedBy;
    private Date updated;

    private Long studentId;
    private Long topicId;

    private String studentName;
    private String topicName;
}
