package com.hau.ketnguyen.it.controller.hau;

import com.hau.ketnguyen.it.controller.APIController;
import com.hau.ketnguyen.it.model.dto.hau.TopicDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchTopicRequest;
import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.TopicService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Topic Controller", description = "Các APIs quản lý đề tài")
@RequestMapping("/topics")
@AllArgsConstructor
@Slf4j
public class TopicController extends APIController<TopicDTO, SearchTopicRequest> {
    private final TopicService topicService;

    @Override
    public ResponseEntity<APIResponse<TopicDTO>> save(TopicDTO topicDTO) {
        return ResponseEntity.ok(APIResponse.success(topicService.save(topicDTO)));
    }

    @Override
    public ResponseEntity<APIResponse<TopicDTO>> edit(Long id, TopicDTO topicDTO) {
        return ResponseEntity.ok(APIResponse.success(topicService.edit(id, topicDTO)));
    }

    @Override
    public ResponseEntity<APIResponse<Void>> delete(Long id) {
        topicService.delete(id);
        return ResponseEntity.ok(APIResponse.success());
    }

    @Override
    public ResponseEntity<APIResponse<TopicDTO>> findById(Long id) {
        return ResponseEntity.ok(APIResponse.success(topicService.findById(id)));
    }

    @Override
    public ResponseEntity<APIResponse<PageDataResponse<TopicDTO>>> getAll(SearchTopicRequest request) {
        return ResponseEntity.ok(APIResponse.success(topicService.getAll(request)));
    }
}
