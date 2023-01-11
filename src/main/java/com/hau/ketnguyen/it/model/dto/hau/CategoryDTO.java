package com.hau.ketnguyen.it.model.dto.hau;

import lombok.Data;

import java.util.Date;

@Data
public class CategoryDTO {
    private Long id;
    private String createdBy;
    private Date created;
    private String updatedBy;
    private Date updated;
    private String name;
    private String code;
    private String description;
}
