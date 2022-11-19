package com.hau.ketnguyen.it.model.dto.hau;

import com.hau.ketnguyen.it.model.dto.BaseDTO;
import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class StudentDTO extends BaseDTO {
    private Integer userId;
    private Long topicId;
    private Long classId;

    private UserDTO userDTO;
    private TopicDTO topicDTO;
    private ClassDTO classDTO;
}
