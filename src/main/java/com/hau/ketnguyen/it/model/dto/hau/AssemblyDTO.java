package com.hau.ketnguyen.it.model.dto.hau;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class AssemblyDTO {
    private Long id;
    private String createdBy;
    private Date created;
    private String updatedBy;
    private Date updated;

    private String nameAssembly;
    private String lecturerIds;
    private List<Long> idLectures;
    private Long topicId;
    private Float score;

    private String topicIds;
    private List<Long> idTopics;

    private TopicDTO topicDTO;

    private List<String> topicNames;
    private List<LecturerDTO> lecturerDTOS;

    private Long lecturePresidentId;
    private Long lectureSecretaryId;
    private String lecturePresidentName;
    private String lectureSecretaryName;
}
