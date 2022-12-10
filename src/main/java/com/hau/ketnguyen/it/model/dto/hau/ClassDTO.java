package com.hau.ketnguyen.it.model.dto.hau;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class ClassDTO {
    private Long id;
    private String createdBy;
    private Date created;
    private String updatedBy;
    private Date updated;
    private String name;
    private String code;
    private Integer stdNumber;
    private Long facultyId;
    private List<StudentDTO> stds;
    private FacultyDTO facultyDTO;
}
