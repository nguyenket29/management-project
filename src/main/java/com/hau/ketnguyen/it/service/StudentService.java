package com.hau.ketnguyen.it.service;

import com.hau.ketnguyen.it.model.dto.hau.StudentDTO;
import com.hau.ketnguyen.it.model.dto.hau.TopicDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchStudentRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchTopicStudentRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;

import java.util.Date;

public interface StudentService extends GenericService<StudentDTO, SearchStudentRequest> {
    void studentRegistryTopic(Long topicId, boolean registry);
    PageDataResponse<TopicDTO> getListTopicRegistry(SearchTopicStudentRequest request);
    PageDataResponse<TopicDTO> getTopicOfStudentApproved(SearchTopicStudentRequest request);
    void studentSuggestTopic(String topicName, Date year, String description);
    void adminApproveTopicSuggest(Long topicId);
    PageDataResponse<TopicDTO> getListTopicSuggestOfStudent(SearchTopicStudentRequest request);
    PageDataResponse<StudentDTO> getAllStudentByLectureGuide(SearchStudentRequest request);
    PageDataResponse<StudentDTO> getAllStudentByLectureCounterArgument(SearchStudentRequest request);
}
