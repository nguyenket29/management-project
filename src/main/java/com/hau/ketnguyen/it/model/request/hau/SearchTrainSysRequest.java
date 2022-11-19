package com.hau.ketnguyen.it.model.request.hau;

import lombok.Data;

@Data
public class SearchTrainSysRequest extends BaseRequest {
    private String name;
    private String code;
}
