package com.hau.ketnguyen.it.model.dto.excel;

import lombok.Data;

import java.time.Instant;

@Data
public class StudentExcelDTO {
    private String codeStudent;
    private String fullName;
    private String gender;
    private Instant dateOfBirth;
    private String address;
    private String town;
    private String className;
    private String topicName;
}
