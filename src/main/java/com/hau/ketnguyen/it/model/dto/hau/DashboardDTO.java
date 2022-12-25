package com.hau.ketnguyen.it.model.dto.hau;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardDTO {
    private Long qtyFaculty;
    private Long qtyClass;
    private Long qtyStudent;
    private Long qtyLecturer;
    private Long qtyAssembly;
    private Long qtyTopic;
    private Long qtyTopicPass;
    private Long qtyTopicNotPass;
    private Long qtyStudentPass;
    private Long qtyStudentNotPass;
}
