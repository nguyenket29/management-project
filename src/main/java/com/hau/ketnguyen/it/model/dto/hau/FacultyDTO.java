package com.hau.ketnguyen.it.model.dto.hau;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class FacultyDTO {
    private Long id;
    private String createdBy;
    private Date created;
    private String updatedBy;
    private Date updated;
    private String specialization;
    private String name;
    private String code;
    private Long workplaceId;
    private WorkplaceDTO workplaceDTO;
}
