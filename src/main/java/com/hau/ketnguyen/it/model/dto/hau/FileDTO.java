package com.hau.ketnguyen.it.model.dto.hau;

import com.hau.ketnguyen.it.model.dto.BaseDTO;
import lombok.Builder;
import lombok.Data;

@Data
public class FileDTO extends BaseDTO {
    private String contentType;
    private String name;
    private String path;
    private String extention;
    private String type;
}
