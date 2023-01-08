package com.hau.ketnguyen.it.model.dto.hau;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private byte[] fileContent;
}
