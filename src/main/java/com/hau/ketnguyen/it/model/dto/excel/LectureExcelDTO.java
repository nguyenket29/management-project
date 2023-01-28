package com.hau.ketnguyen.it.model.dto.excel;

import lombok.Data;

import java.time.Instant;

@Data
public class LectureExcelDTO {
    private String codeLecture;
    private String fullName;
    private String gender;
    private Instant dateOfBirth;
    private String address;
    private String degree;
    private String facultyName;
    private String regency;
    private String town;
}
