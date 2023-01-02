package com.hau.ketnguyen.it.model.request.hau;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class SearchTopicRequest extends BaseRequest {
    private String name;
    private Integer stdNumber;
    private Long fileId;
    private Long srcCodeId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date year;
    private String description;
    private boolean status;
    private Float score;
    private Long lecturerGuideId;
    private Long lecturerCounterArgumentId;
}
