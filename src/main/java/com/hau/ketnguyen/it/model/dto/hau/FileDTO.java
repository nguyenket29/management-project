package com.hau.ketnguyen.it.model.dto.hau;

import lombok.Data;

import java.util.Date;

@Data
public class FileDTO {
    private Long id;
    private String createdBy;
    private Date created;
    private String updatedBy;
    private Date updated;
    private String contentType;
    private String name;
    private String path;
    private String extention;
    private String type;
}
