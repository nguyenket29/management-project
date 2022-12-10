package com.hau.ketnguyen.it.model.dto.hau;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class WorkplaceDTO {
    private Long id;
    private String createdBy;
    private Date created;
    private String updatedBy;
    private Date updated;
    private String name;
    private String address;
    private String email;
    private String phoneNumber;
}
