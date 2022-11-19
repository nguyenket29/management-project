package com.hau.ketnguyen.it.model.request.hau;

import lombok.Data;

import java.util.Date;

@Data
public abstract class BaseRequest {
    private Long id;
    private String createdBy;
    private Date created;
    private String updatedBy;
    private Date updated;
    private int page = 0;
    private int size = 10;
}
