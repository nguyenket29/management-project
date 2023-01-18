package com.hau.ketnguyen.it.service.mapper;

import com.hau.ketnguyen.it.entity.hau.Lecturers;
import com.hau.ketnguyen.it.entity.hau.StudentTopic;
import com.hau.ketnguyen.it.model.dto.hau.LecturerDTO;
import com.hau.ketnguyen.it.model.dto.hau.StudentTopicDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentTopicMapper extends EntityMapper<StudentTopicDTO, StudentTopic> {
}
