package com.hau.ketnguyen.it.controller.hau;

import com.hau.ketnguyen.it.model.dto.hau.LecturerDTO;
import com.hau.ketnguyen.it.model.dto.hau.StudentTopicDTO;
import com.hau.ketnguyen.it.model.dto.hau.TopicDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchLecturerRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchStudentTopicRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchTopicRequest;
import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.LecturerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "Lecturer Controller", description = "Các APIs quản lý giảng viên")
@RequestMapping("/lecturers")
@AllArgsConstructor
@Slf4j
public class LecturerController {
    private final LecturerService lecturerService;

    @PostMapping
    public ResponseEntity<APIResponse<LecturerDTO>> save(@RequestBody LecturerDTO lecturerDTO) {
        return ResponseEntity.ok(APIResponse.success(lecturerService.save(lecturerDTO)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<LecturerDTO>> edit(@PathVariable Long id, @RequestBody LecturerDTO lecturerDTO) {
        return ResponseEntity.ok(APIResponse.success(lecturerService.edit(id, lecturerDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> delete(@PathVariable Long id) {
        lecturerService.delete(id);
        return ResponseEntity.ok(APIResponse.success());
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<LecturerDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(APIResponse.success(lecturerService.findById(id)));
    }

    @GetMapping("/get-list-topic-counter")
    @ApiOperation(value = "Lấy danh sách đề tài mà giảng viên đó phản biện")
    public ResponseEntity<APIResponse<PageDataResponse<TopicDTO>>> getListTopicCounterArgument(SearchTopicRequest request) {
        return ResponseEntity.ok(APIResponse.success(lecturerService.getListTopicCounterArgument(request)));
    }

    @GetMapping("/get-list-topic-guide")
    @ApiOperation(value = "Lấy danh sách đề tài mà giảng viên đó hướng dẫn")
    public ResponseEntity<APIResponse<PageDataResponse<TopicDTO>>> getListTopicGuide(SearchTopicRequest request) {
        return ResponseEntity.ok(APIResponse.success(lecturerService.getListTopicGuide(request)));
    }

    @GetMapping("/get-list-topic")
    @ApiOperation(value = "Lấy danh sách đề tài mà giảng viên đó hướng dẫn đuợc sinh viên đăng ký")
    public ResponseEntity<APIResponse<PageDataResponse<StudentTopicDTO>>> getListStudentRegistryTopic(SearchStudentTopicRequest request) {
        return ResponseEntity.ok(APIResponse.success(lecturerService.getListStudentRegistryTopic(request)));
    }

    @GetMapping("/approve-topic")
    @ApiOperation(value = "Giảng viên duyệt đề tài cho sinh viên")
    public ResponseEntity<APIResponse<Void>> approveTopicForStudent(@RequestParam Long topicId, @RequestParam Long studentId) {
        lecturerService.approveTopicForStudent(topicId, studentId);
        return ResponseEntity.ok(APIResponse.success());
    }

    @GetMapping("/check-lecture-by-assembly")
    @ApiOperation(value = "Kiểm tra giảng viên đó có phải chủ tịch hội đồng hay không")
    public ResponseEntity<APIResponse<Void>> checkLectureHavePresidentAssembly(@RequestParam Integer userId) {
        lecturerService.checkLectureInAssembly(userId);
        return ResponseEntity.ok(APIResponse.success());
    }

    @GetMapping("/get-topic-by-assembly")
    @ApiOperation(value = "Lấy danh sách đề tài theo giảng viên hội dồng")
    public ResponseEntity<APIResponse<PageDataResponse<TopicDTO>>> getListTopicOfPresidentAssembly(SearchTopicRequest request) {
        return ResponseEntity.ok(APIResponse.success(lecturerService.getListTopicOfPresidentAssembly(request)));
    }

    @GetMapping
    public ResponseEntity<APIResponse<PageDataResponse<LecturerDTO>>> getAll(SearchLecturerRequest request) {
        return ResponseEntity.ok(APIResponse.success(lecturerService.getAll(request)));
    }
}
