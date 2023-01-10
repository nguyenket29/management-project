package com.hau.ketnguyen.it.model.dto.hau;

import lombok.Data;

import java.util.Date;

@Data
public class StatisticalDTO {
    private String nameTopic;
    private String nameStudent;
    private String nameClass;
    private Date topicYear;
    private Float scoreGuide;
    private Float scoreCounterArgument;
    private Float scoreAssembly;
    private Float scoreMedium;
}
