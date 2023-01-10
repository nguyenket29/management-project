package com.hau.ketnguyen.it.service;

import com.hau.ketnguyen.it.model.dto.hau.StatisticalDTO;
import com.hau.ketnguyen.it.model.dto.hau.TopicDTO;
import com.hau.ketnguyen.it.model.request.auth.SearchRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchTopicRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TopicService extends GenericService<TopicDTO, SearchTopicRequest> {
    void uploadFile(MultipartFile[] file, String filePath, boolean isPublic, Long topicId);
    PageDataResponse<StatisticalDTO> getStatistical(SearchRequest request);
}
