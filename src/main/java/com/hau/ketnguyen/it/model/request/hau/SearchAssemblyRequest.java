package com.hau.ketnguyen.it.model.request.hau;

import lombok.Data;

import java.util.List;

@Data
public class SearchAssemblyRequest extends BaseRequest {
    private Long topicId;
    private Long commentId;
    private Long lecturePresidentId;
    private Long lectureSecretaryId;
    private List<Long> lectureIds;
    private String nameAssembly;
}
