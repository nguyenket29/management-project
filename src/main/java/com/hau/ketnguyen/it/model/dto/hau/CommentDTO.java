package com.hau.ketnguyen.it.model.dto.hau;

import com.hau.ketnguyen.it.model.dto.BaseDTO;
import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDTO extends BaseDTO {
    private String content;
    private Long userId;
    private Long topicId;
    private UserDTO userDTO;
    private TopicDTO topicDTO;
}
