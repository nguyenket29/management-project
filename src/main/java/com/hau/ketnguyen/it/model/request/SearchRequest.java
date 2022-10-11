package com.hau.ketnguyen.it.model.request;

import lombok.Data;

@Data
public class SearchRequest {
    private int page = 0;
    private int size = 10;
}
