package com.hau.ketnguyen.it.model.request.hau;

import lombok.Data;

import java.util.Date;

@Data
public class SearchStudentRequest extends BaseRequest {
    private String name;
    private short gender;
    private Date dateOfBirth;
    private String address;
    private String email;
    private String phoneNumber;
    private Long topicId;
    private Long classId;
}
