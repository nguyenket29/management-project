package com.hau.ketnguyen.it.model.request.hau;

import lombok.Data;

import java.util.Date;

@Data
public class SearchLecturerRequest extends BaseRequest {
    private String name;
    private String code;
    private short gender;
    private Date dateOfBirth;
    private String address;
    private String email;
    private String phoneNumber;
    //chức vụ
    private String regency;
    //cấp bậc
    private String degree;
    private Long facultyId;
    private Long workplaceId;
}
