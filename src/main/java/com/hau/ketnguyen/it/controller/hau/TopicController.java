package com.hau.ketnguyen.it.controller.hau;

import com.hau.ketnguyen.it.model.dto.hau.StatisticalDTO;
import com.hau.ketnguyen.it.model.dto.hau.TopicDTO;
import com.hau.ketnguyen.it.model.request.auth.SearchRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchTopicRequest;
import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.TopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Api(value = "Topic Controller", description = "Các APIs quản lý đề tài")
@RequestMapping("/topics")
@AllArgsConstructor
@Slf4j
public class TopicController {
    private final TopicService topicService;

    @PostMapping
    public ResponseEntity<APIResponse<TopicDTO>> save(@RequestBody TopicDTO topicDTO) {
        return ResponseEntity.ok(APIResponse.success(topicService.save(topicDTO)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<TopicDTO>> edit(@PathVariable Long id,@RequestBody TopicDTO topicDTO) {
        return ResponseEntity.ok(APIResponse.success(topicService.edit(id, topicDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> delete(@PathVariable Long id) {
        topicService.delete(id);
        return ResponseEntity.ok(APIResponse.success());
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<TopicDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(APIResponse.success(topicService.findById(id)));
    }

    @GetMapping("/get-list-topic-suggest")
    @ApiOperation(value = "Quản trị viên lấy danh sách đề tài sinh viên đề xuất")
    public ResponseEntity<APIResponse<PageDataResponse<TopicDTO>>> getListTopicSuggest(SearchTopicRequest request) {
        return ResponseEntity.ok(APIResponse.success(topicService.getListTopicSuggest(request)));
    }

    @GetMapping
    public ResponseEntity<APIResponse<PageDataResponse<TopicDTO>>> getAll(SearchTopicRequest request) {
        return ResponseEntity.ok(APIResponse.success(topicService.getAll(request)));
    }

    @GetMapping("/statistical-score")
    public ResponseEntity<APIResponse<PageDataResponse<StatisticalDTO>>> getScoreByTopic(SearchRequest request) {
        return ResponseEntity.ok(APIResponse.success(topicService.getStatistical(request)));
    }

    @PostMapping("/upload")
    public ResponseEntity<APIResponse<List<String>>> uploadAvatar(@RequestParam("fileUpload") MultipartFile[] fileUpload,
                                                          @RequestParam("filePath") String pathFile,
                                                          @RequestParam("shared") String shared,
                                                          @RequestParam("topicId") Long topicId) {
        return ResponseEntity.ok(APIResponse.success(topicService.uploadFile(fileUpload, pathFile, Boolean.parseBoolean(shared), topicId)));
    }
}
