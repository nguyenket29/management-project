package com.hau.ketnguyen.it.model.dto.hau;

import lombok.Data;

import java.util.Date;

@Data
public class TopicSuggestDTO {
    private String topicName;
    private Date year;
    private String description;
}
