package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.model.dto.hau.TopicDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchTopicRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.TopicService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class TopicServiceImpl implements TopicService {
    @Override
    public TopicDTO save(TopicDTO topicDTO) {
        return null;
    }

    @Override
    public TopicDTO edit(Long id, TopicDTO topicDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public TopicDTO findById(Long id) {
        return null;
    }

    @Override
    public PageDataResponse<TopicDTO> getAll(SearchTopicRequest request) {
        return null;
    }
}
