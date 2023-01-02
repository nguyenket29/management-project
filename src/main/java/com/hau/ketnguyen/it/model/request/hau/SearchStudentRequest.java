package com.hau.ketnguyen.it.model.request.hau;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.util.Date;

@Data
public class SearchStudentRequest extends BaseRequest {
    private String fullName;
    private Short gender;
    private Instant dateOfBirth;
    private String address;
    private String codeStudent;
    private Boolean stdPass;
    private String email;
    private String phoneNumber;
    private Long topicId;
    private Long classId;
}
