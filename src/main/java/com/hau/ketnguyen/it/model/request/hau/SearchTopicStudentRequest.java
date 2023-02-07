package com.hau.ketnguyen.it.model.request.hau;

import com.hau.ketnguyen.it.model.request.auth.SearchRequest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class SearchTopicStudentRequest extends SearchRequest {
    private String topicName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date year;
    private String description;
    private Long lecturerGuideId;
    private Long lecturerCounterArgumentId;
    private Long categoryId;
    private List<Long> topicIds = new ArrayList<>();
}
