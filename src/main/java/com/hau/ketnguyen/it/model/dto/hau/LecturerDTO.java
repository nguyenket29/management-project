package com.hau.ketnguyen.it.model.dto.hau;

import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import com.hau.ketnguyen.it.model.dto.auth.UserInfoDTO;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
@Builder
public class LecturerDTO {
    private Long id;
    private String createdBy;
    private Date created;
    private String updatedBy;
    private Date updated;

    // user info
    private String fullName;
    private String avatar;
    private Instant dateOfBirth;
    private String town;
    private short gender;
    private String address;
    private String phoneNumber;

    private Integer userId;
    private Long userInfoId;
    private String codeLecture;
    //chức vụ
    private String regency;
    //cấp bậc
    private String degree;
    private Long facultyId;

    private UserInfoDTO userInfoDTO;
    private FacultyDTO facultyDTO;
}
