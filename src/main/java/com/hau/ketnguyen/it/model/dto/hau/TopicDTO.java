package com.hau.ketnguyen.it.model.dto.hau;

import com.hau.ketnguyen.it.model.dto.BaseDTO;
import lombok.Data;

import java.util.Date;

@Data
public class TopicDTO extends BaseDTO {
    private String name;
    private Integer stdNumber;
    private Long fileId;
    private Long srcCodeId;
    private Date year;
    private Float score;
    private Long lecturerId;
}
