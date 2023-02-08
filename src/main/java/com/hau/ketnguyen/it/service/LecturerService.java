package com.hau.ketnguyen.it.service;

import com.hau.ketnguyen.it.model.dto.hau.LecturerDTO;
import com.hau.ketnguyen.it.model.dto.hau.StudentDTO;
import com.hau.ketnguyen.it.model.dto.hau.StudentTopicDTO;
import com.hau.ketnguyen.it.model.dto.hau.TopicDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchLecturerRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchStudentTopicRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchTopicRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;

import java.util.List;

public interface LecturerService extends GenericService<LecturerDTO, SearchLecturerRequest> {
    PageDataResponse<TopicDTO> getListTopicCounterArgument(SearchTopicRequest request);
    PageDataResponse<TopicDTO> getListTopicGuide(SearchTopicRequest request);
    PageDataResponse<StudentTopicDTO> getListStudentRegistryTopic(SearchStudentTopicRequest request);
    void approveTopicForStudent(Long topicId, Long studentId);
    boolean checkLectureInAssembly(Integer userId);
    PageDataResponse<TopicDTO> getListTopicOfPresidentAssembly(SearchTopicRequest request);
}
