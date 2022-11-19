package com.hau.ketnguyen.it.model.dto.hau;

import com.hau.ketnguyen.it.model.dto.BaseDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClassDTO extends BaseDTO {
    private String name;
    private String code;
    private Integer stdNumber;
    private Long facultyId;
    private List<StudentDTO> stds;
    private FacultyDTO facultyDTO;
}
