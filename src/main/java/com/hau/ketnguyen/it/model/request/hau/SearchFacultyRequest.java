package com.hau.ketnguyen.it.model.request.hau;

import lombok.Data;

@Data
public class SearchFacultyRequest extends BaseRequest {
    private String name;
    private String code;
    private Long workplaceId;
}
