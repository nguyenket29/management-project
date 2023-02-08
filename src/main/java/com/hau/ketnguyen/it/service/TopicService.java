package com.hau.ketnguyen.it.service;

import com.hau.ketnguyen.it.model.dto.hau.StatisticalDTO;
import com.hau.ketnguyen.it.model.dto.hau.StudentSuggestTopicDTO;
import com.hau.ketnguyen.it.model.dto.hau.TopicDTO;
import com.hau.ketnguyen.it.model.request.auth.SearchRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchStudentTopicRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchTopicRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TopicService extends GenericService<TopicDTO, SearchTopicRequest> {
    List<String> uploadFile(MultipartFile[] file, String filePath, boolean isPublic, Long topicId);
    PageDataResponse<StatisticalDTO> getStatistical(SearchRequest request);
    PageDataResponse<TopicDTO> getListTopicSuggest(SearchTopicRequest request);
    PageDataResponse<StudentSuggestTopicDTO> getListStudentSuggestTopic(SearchStudentTopicRequest request);
    PageDataResponse<TopicDTO> getTopicDTOPageDataResponse(Page<TopicDTO> page);
}
