package com.hau.ketnguyen.it.model.request.hau;

import lombok.Data;

@Data
public class SearchAssemblyRequest extends BaseRequest {
    private Long topicId;
    private Long commentId;
    private String nameAssembly;
}
