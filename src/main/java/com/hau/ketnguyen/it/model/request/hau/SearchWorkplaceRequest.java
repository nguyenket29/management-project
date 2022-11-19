package com.hau.ketnguyen.it.model.request.hau;

import lombok.Data;

@Data
public class SearchWorkplaceRequest extends BaseRequest {
    private String name;
    private String address;
    private String email;
    private String phoneNumber;
}
