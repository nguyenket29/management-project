package com.hau.ketnguyen.it.service;

import com.hau.ketnguyen.it.model.dto.hau.TopicDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchTopicRequest;
import org.springframework.web.multipart.MultipartFile;

public interface TopicService extends GenericService<TopicDTO, SearchTopicRequest> {
    void uploadFile(MultipartFile file, String filePath, boolean isPublic, Long topicId);
}
