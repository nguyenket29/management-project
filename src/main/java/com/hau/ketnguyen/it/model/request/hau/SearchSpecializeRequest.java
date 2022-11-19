package com.hau.ketnguyen.it.model.request.hau;

import lombok.Data;

@Data
public class SearchSpecializeRequest extends BaseRequest {
    private String name;
    private Long facultyId;
    private Long classId;
    private Long trainSystemId;
}
