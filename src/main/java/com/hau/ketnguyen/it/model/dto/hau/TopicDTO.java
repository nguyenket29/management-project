package com.hau.ketnguyen.it.model.dto.hau;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

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
    private Boolean status;
    private String description;
    private Date year;
    private Float scoreGuide;
    private Float scoreCounterArgument;
    private Long lecturerGuideId;
    private Long lecturerCounterArgumentId;
    private LecturerDTO lecturerGuideDTO;
    private LecturerDTO lecturerCounterArgumentDTO;
    private Float scoreProcessOne;
    private Float scoreProcessTwo;
    private Long categoryId;
    private String categoryName;
    private boolean studentRegistry;
    private boolean studentApprove;

    private List<String> fileIds;
}
