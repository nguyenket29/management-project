package com.hau.ketnguyen.it.model.dto.excel;

import lombok.Data;

import java.util.List;

@Data
public class AssemblyExcelDTO {
    private String nameAssembly;
    private List<String> lecturerNames;
    private String topicName;
    private Float score;
}
