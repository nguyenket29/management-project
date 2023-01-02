package com.hau.ketnguyen.it.model.dto.hau;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class StatisticalDTO {
    private String nameTopic;
    private String nameStudent;
    private String nameClass;
    private Date year;
    private Float scoreGuide;
    private Float scoreCounterArgument;
    private Float scoreAssembly;
    private Float scoreMedium;
}
