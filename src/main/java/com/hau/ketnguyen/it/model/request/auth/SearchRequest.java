package com.hau.ketnguyen.it.model.request.auth;

import lombok.Data;

@Data
public class SearchRequest {
    private int page = 0;
    private int size = 10;
}
