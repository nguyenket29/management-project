package com.hau.ketnguyen.it.model.dto.hau;

import com.hau.ketnguyen.it.model.dto.BaseDTO;
import lombok.Data;

import java.util.Date;

@Data
public class LecturerDTO extends BaseDTO {
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
