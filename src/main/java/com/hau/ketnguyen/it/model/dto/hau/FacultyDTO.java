package com.hau.ketnguyen.it.model.dto.hau;

import com.hau.ketnguyen.it.model.dto.BaseDTO;
import lombok.Data;

@Data
public class FacultyDTO extends BaseDTO {
    private String name;
    private String code;
    private Long workplaceId;
}
