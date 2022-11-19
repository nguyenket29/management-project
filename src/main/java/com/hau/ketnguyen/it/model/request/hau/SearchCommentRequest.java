package com.hau.ketnguyen.it.model.request.hau;

import lombok.Data;

@Data
public class SearchCommentRequest extends BaseRequest {
    private String content;
    private Long userId;
    private Long topicId;
    private Long assemblyId;
}
