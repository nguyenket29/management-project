package com.hau.ketnguyen.it.model.dto.excel;

import lombok.Data;

import java.util.Date;

@Data
public class TopicExcelDTO {
    private String name;
    private String categoryName;
    private String lecturerCounterArgumentName;
    private String lecturerGuideName;
    private Float scoreCounterArgument;
    private Float scoreGuide;
    private Float scoreProcessOne;
    private Float scoreProcessTwo;
    private String status;
    private Integer stdNumber;
    private Date year;
    private String description;
}
