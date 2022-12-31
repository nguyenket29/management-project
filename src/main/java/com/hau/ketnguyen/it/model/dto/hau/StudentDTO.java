package com.hau.ketnguyen.it.model.dto.hau;

import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import com.hau.ketnguyen.it.model.dto.auth.UserInfoDTO;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
@Builder
public class StudentDTO {
    private Long id;
    private String createdBy;
    private Date created;
    private String updatedBy;
    private Date updated;

    private String fullName;
    private String avatar;
    private Instant dateOfBirth;
    private String town;
    private short gender;
    private String address;
    private String phoneNumber;

    private Integer userId;
    private Long userInfoId;
    private String codeStudent;
    private Long topicId;
    private Long classId;
    private boolean stdPass;

    private UserInfoDTO userInfoDTO;
    private TopicDTO topicDTO;
    private ClassDTO classDTO;
}
