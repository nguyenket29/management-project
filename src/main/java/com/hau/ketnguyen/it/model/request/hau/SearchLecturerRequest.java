package com.hau.ketnguyen.it.model.request.hau;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.Instant;
import java.util.Date;

@Data
public class SearchLecturerRequest extends BaseRequest {
    private String codeLecture;
    private String fullName;
    private Short gender;
    private Instant dateOfBirth;
    private String address;
    private String email;
    private String phoneNumber;
    //chức vụ
    private String regency;
    //cấp bậc
    private String degree;
    private Long facultyId;
    private String town;
}
