package com.hau.ketnguyen.it.service.mapper;

import com.hau.ketnguyen.it.entity.hau.StudentSuggestTopic;
import com.hau.ketnguyen.it.entity.hau.StudentTopic;
import com.hau.ketnguyen.it.model.dto.hau.StudentSuggestTopicDTO;
import com.hau.ketnguyen.it.model.dto.hau.StudentTopicDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentSuggestTopicMapper extends EntityMapper<StudentSuggestTopicDTO, StudentSuggestTopic> {
}
