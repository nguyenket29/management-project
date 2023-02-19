package com.hau.ketnguyen.it.controller.hau;

import com.hau.ketnguyen.it.model.dto.hau.StatisticalDTO;
import com.hau.ketnguyen.it.model.dto.hau.StudentSuggestTopicDTO;
import com.hau.ketnguyen.it.model.dto.hau.TopicDTO;
import com.hau.ketnguyen.it.model.request.auth.SearchRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchStudentTopicRequest;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@Api(value = "Topic Controller", description = "Các APIs quản lý đề tài")
@RequestMapping("/topics")
@AllArgsConstructor
@Slf4j
public class TopicController {
    private final TopicService topicService;

    @PostMapping
    @ApiOperation("Api tạo mới đề tài")
    public ResponseEntity<APIResponse<TopicDTO>> save(@RequestBody TopicDTO topicDTO) {
        return ResponseEntity.ok(APIResponse.success(topicService.save(topicDTO)));
    }

    @PutMapping("/{id}")
    @ApiOperation("Api cập nhật đề tài")
    public ResponseEntity<APIResponse<TopicDTO>> edit(@PathVariable Long id,@RequestBody TopicDTO topicDTO) {
        return ResponseEntity.ok(APIResponse.success(topicService.edit(id, topicDTO)));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Api xóa đề tài")
    public ResponseEntity<APIResponse<Void>> delete(@PathVariable Long id) {
        topicService.delete(id);
        return ResponseEntity.ok(APIResponse.success());
    }

    @GetMapping("/{id}")
    @ApiOperation("Api xem chi tiết đề tài")
    public ResponseEntity<APIResponse<TopicDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(APIResponse.success(topicService.findById(id)));
    }

    @GetMapping("/get-list-topic-suggest")
    @ApiOperation(value = "Quản trị viên lấy danh sách đề tài sinh viên đề xuất")
    public ResponseEntity<APIResponse<PageDataResponse<TopicDTO>>> getListTopicSuggest(SearchTopicRequest request) {
        return ResponseEntity.ok(APIResponse.success(topicService.getListTopicSuggest(request)));
    }

    @GetMapping("/get-topic-suggest")
    @ApiOperation(value = "Lấy danh sách đề tài đề xuất cho người quản trị")
    public ResponseEntity<APIResponse<PageDataResponse<StudentSuggestTopicDTO>>> getListStudentSuggestTopic(SearchStudentTopicRequest request) {
        return ResponseEntity.ok(APIResponse.success(topicService.getListStudentSuggestTopic(request)));
    }

    @GetMapping
    @ApiOperation("Api lấy danh sách đề tài")
    public ResponseEntity<APIResponse<PageDataResponse<TopicDTO>>> getAll(SearchTopicRequest request) {
        return ResponseEntity.ok(APIResponse.success(topicService.getAll(request)));
    }

    @ApiOperation(value = "Api thống kê điểm")
    @GetMapping("/statistical-score")
    public ResponseEntity<APIResponse<PageDataResponse<StatisticalDTO>>> getScoreByTopic(SearchTopicRequest request) {
        return ResponseEntity.ok(APIResponse.success(topicService.getStatistical(request)));
    }

    @ApiOperation("Api tải lên file đề tài lên GG driver")
    @PostMapping("/upload")
    public ResponseEntity<APIResponse<List<String>>> uploadAvatar(@RequestParam("fileUpload") MultipartFile[] fileUpload,
                                                          @RequestParam("filePath") String pathFile,
                                                          @RequestParam("shared") String shared,
                                                          @RequestParam("topicId") Long topicId) {
        return ResponseEntity.ok(APIResponse.success(topicService.uploadFile(fileUpload, pathFile, Boolean.parseBoolean(shared), topicId)));
    }

    @ApiOperation("Api tải lên file đề tài")
    @PostMapping("/upload-local")
    public ResponseEntity<APIResponse<List<String>>> uploadAvatar(@RequestParam("fileUpload") MultipartFile[] fileUpload,
                                                                  @RequestParam("topicId") Long topicId) throws IOException {
        return ResponseEntity.ok(APIResponse.success(topicService.uploadFileLocal(fileUpload, topicId)));
    }

    @ApiOperation("Api tải xuống file đề tài")
    @GetMapping(value = "/download/{id}")
    public ResponseEntity<byte[]> downFileStringId(@PathVariable("id") String id, HttpServletResponse response) throws Exception {
        return ResponseEntity.ok().body(topicService.downFileFromLocal(id, response).getFileContent());
    }
}
