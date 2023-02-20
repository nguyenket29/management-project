package com.hau.ketnguyen.it.model.request.hau;

import lombok.Data;

import java.util.Date;

@Data
public class SearchStatisticalRequest extends BaseRequest {
    private String nameClass;
    private String nameStudent;
    private String nameTopic;
    private Float scoreAssembly = (float) 0;
    private Float scoreCounterArgument = (float) 0;
    private Float scoreGuide = (float) 0;
    private Float scoreProcessOne = (float) 0;
    private Float scoreProcessTwo = (float) 0;
    private Float scoreMedium = (float) 0;
    private Date topicYear;
}
