package com.hau.ketnguyen.it.model.dto.hau;

import com.hau.ketnguyen.it.model.dto.BaseDTO;
import lombok.Data;

import java.util.Date;

@Data
public class StudentDTO extends BaseDTO {
    private String name;
    private short gender;
    private Date dateOfBirth;
    private String address;
    private String email;
    private String phoneNumber;
    private Long topicId;
    private Long classId;
}
