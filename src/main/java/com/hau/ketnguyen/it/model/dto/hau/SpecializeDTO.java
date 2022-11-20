package com.hau.ketnguyen.it.model.dto.hau;

import com.hau.ketnguyen.it.model.dto.BaseDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpecializeDTO extends BaseDTO {
    private String name;
    private Long facultyId;
    private Long trainSystemId;
    private FacultyDTO facultyDTO;
    private TrainingSystemDTO trainingSystemDTO;
}
