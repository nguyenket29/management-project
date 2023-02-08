package com.hau.ketnguyen.it.model.dto.excel;

import lombok.Data;

import java.util.List;

@Data
public class AssemblyExcelDTO {
    private String nameAssembly;
    private String lecturePresidentName;
    private String lectureSecretaryName;
    private List<String> lecturerNames;
    private List<String> topicNames;
}
