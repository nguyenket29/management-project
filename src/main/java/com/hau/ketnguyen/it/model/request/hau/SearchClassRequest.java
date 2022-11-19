package com.hau.ketnguyen.it.model.request.hau;

import lombok.Data;

@Data
public class SearchClassRequest extends BaseRequest {
    private String name;
    private String code;
    private Integer stdNumber;
    private Long facultyId;
}
