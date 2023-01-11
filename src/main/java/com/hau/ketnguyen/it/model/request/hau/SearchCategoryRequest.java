package com.hau.ketnguyen.it.model.request.hau;

import lombok.Data;

@Data
public class SearchCategoryRequest extends BaseRequest {
    private String name;
    private String code;
    private String description;
}
