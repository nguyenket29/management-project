package com.hau.ketnguyen.it.model.dto.hau;

import com.hau.ketnguyen.it.model.dto.BaseDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainingSystemDTO extends BaseDTO {
    private String name;
    private String code;
}
