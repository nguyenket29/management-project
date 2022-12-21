package com.hau.ketnguyen.it.model.dto.hau;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AssemblyDTO {
    private Long id;
    private String createdBy;
    private Date created;
    private String updatedBy;
    private Date updated;
    private Long lecturerId;
    private Long topicId;
    private Float score;

    private TopicDTO topicDTO;
    private LecturerDTO lecturerDTO;
}
