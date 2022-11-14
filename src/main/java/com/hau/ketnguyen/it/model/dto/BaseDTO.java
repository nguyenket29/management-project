package com.hau.ketnguyen.it.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public abstract class BaseDTO {
    private Long id;
    private String createdBy;
    private Date created;
    private String updatedBy;
    private Date updated;
}
