package com.hau.ketnguyen.it.model.request.hau;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class SearchStudentRequest extends BaseRequest {
    private String fullName;
    private Short gender;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date dateOfBirth;
    private String address;
    private String email;
    private String phoneNumber;
    private Long topicId;
    private Long classId;
}
