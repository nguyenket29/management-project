package com.hau.ketnguyen.it.model.request.hau;

import com.hau.ketnguyen.it.model.request.auth.SearchRequest;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchTopicStudentRequest extends SearchRequest {
    private String topicName;
    private List<Long> topicIds = new ArrayList<>();
}
