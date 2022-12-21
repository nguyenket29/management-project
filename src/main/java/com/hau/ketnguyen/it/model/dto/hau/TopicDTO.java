package com.hau.ketnguyen.it.model.dto.hau;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class TopicDTO {
    private Long id;
    private String createdBy;
    private Date created;
    private String updatedBy;
    private Date updated;
    private String name;
    private Integer stdNumber;
    private String fileId;
    private Date year;
    private Float score;
    private Long lecturerId;
    private LecturerDTO lecturerDTO;
}
