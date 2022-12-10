package com.hau.ketnguyen.it.model.dto.hau;

import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class LecturerDTO {
    private Long id;
    private String createdBy;
    private Date created;
    private String updatedBy;
    private Date updated;
    private Integer userId;
    //chức vụ
    private String regency;
    //cấp bậc
    private String degree;
    private Long facultyId;

    private UserDTO userDTO;
    private FacultyDTO facultyDTO;
}
