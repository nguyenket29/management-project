package com.hau.ketnguyen.it.model.dto.hau;

import com.hau.ketnguyen.it.model.dto.BaseDTO;
import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class LecturerDTO extends BaseDTO {
    private Integer userId;
    //chức vụ
    private String regency;
    //cấp bậc
    private String degree;
    private Long facultyId;
    private Long workplaceId;

    private UserDTO userDTO;
    private FacultyDTO facultyDTO;
    private WorkplaceDTO workplaceDTO;
}
