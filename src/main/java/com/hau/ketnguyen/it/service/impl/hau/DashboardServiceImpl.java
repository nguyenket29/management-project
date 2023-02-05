package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.model.dto.hau.DashboardDTO;
import com.hau.ketnguyen.it.repository.hau.*;
import com.hau.ketnguyen.it.service.DashboardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {
    private final LecturerReps lecturerReps;
    private final StudentReps studentReps;
    private final TopicReps topicReps;
    private final ClassReps classReps;
    private final FacultyReps facultyReps;
    private final AssemblyReps assemblyReps;

    @Override
    public DashboardDTO dashboard() {
        Long qtyLecturer = lecturerReps.count();
        Long qtyStudent = studentReps.count();
        Long qtyClass = classReps.count();
        Long qtyTopic = topicReps.getCount();
        Long qtyAssembly = assemblyReps.count();
        Long qtyFaculty = facultyReps.count();
        Long qtyStdPass = (long) studentReps.findByStdPass(true).size();
        Long qtyStdNotPass = (long) studentReps.findByStdPass(false).size();
        Long qtyTopicPass = (long) topicReps.findByStatus(true).size();
        Long qtyTopicNotPass = (long) topicReps.findByStatus(false).size();

        return DashboardDTO.builder()
                .qtyStudent(qtyStudent)
                .qtyStudentNotPass(qtyStdNotPass)
                .qtyStudentPass(qtyStdPass)
                .qtyTopic(qtyTopic)
                .qtyTopicPass(qtyTopicPass)
                .qtyTopicNotPass(qtyTopicNotPass)
                .qtyAssembly(qtyAssembly)
                .qtyClass(qtyClass)
                .qtyFaculty(qtyFaculty)
                .qtyLecturer(qtyLecturer)
                .build();
    }
}
