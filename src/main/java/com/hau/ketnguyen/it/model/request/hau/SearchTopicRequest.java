package com.hau.ketnguyen.it.model.request.hau;

import lombok.Data;

import java.util.Date;

@Data
public class SearchTopicRequest extends BaseRequest {
    private String name;
    private Integer stdNumber;
    private Long fileId;
    private Long srcCodeId;
    private Date year;
    private Float score;
    private Long lecturerGuideId;
    private Long lecturerCounterArgumentId;
}
