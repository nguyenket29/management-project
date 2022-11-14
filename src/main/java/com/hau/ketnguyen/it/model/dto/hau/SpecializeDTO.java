package com.hau.ketnguyen.it.model.dto.hau;

import com.hau.ketnguyen.it.model.dto.BaseDTO;
import lombok.Data;

@Data
public class SpecializeDTO extends BaseDTO {
    private String name;
    private Long facultyId;
    private Long classId;
    private Long trainSystemId;
}
