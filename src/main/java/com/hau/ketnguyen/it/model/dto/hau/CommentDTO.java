package com.hau.ketnguyen.it.model.dto.hau;

import com.hau.ketnguyen.it.model.dto.BaseDTO;
import lombok.Data;

@Data
public class CommentDTO extends BaseDTO {
    private String content;
    private Long userId;
    private Long topicId;
}
