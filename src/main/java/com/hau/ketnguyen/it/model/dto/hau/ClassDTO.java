package com.hau.ketnguyen.it.model.dto.hau;

import com.hau.ketnguyen.it.model.dto.BaseDTO;
import lombok.Data;

@Data
public class ClassDTO extends BaseDTO {
    private String name;
    private String code;
    private Integer stdNumber;
    private Long facultyId;
}
