package com.hau.ketnguyen.it.model.dto.hau;

import com.hau.ketnguyen.it.model.dto.BaseDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssemblyDTO extends BaseDTO {
    private Long lecturerId;
    private Long topicId;
    private Long commentId;
}
