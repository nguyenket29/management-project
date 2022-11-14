package com.hau.ketnguyen.it.model.dto.hau;

import com.hau.ketnguyen.it.model.dto.BaseDTO;
import lombok.Data;

@Data
public class WorkplaceDTO extends BaseDTO {
    private String name;
    private String address;
    private String email;
    private String phoneNumber;
}
