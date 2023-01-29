package com.hau.ketnguyen.it.controller.hau;

import com.hau.ketnguyen.it.model.dto.hau.StudentDTO;
import com.hau.ketnguyen.it.model.dto.hau.TopicDTO;
import com.hau.ketnguyen.it.model.dto.hau.TopicSuggestDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchStudentRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchTopicStudentRequest;
import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.License;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "Student Controller", description = "Các APIs quản lý sinh viên")
@RequestMapping("/students")
@AllArgsConstructor
@Slf4j
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<APIResponse<StudentDTO>> save(@RequestBody StudentDTO studentDTO) {
        return ResponseEntity.ok(APIResponse.success(studentService.save(studentDTO)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<StudentDTO>> edit(@PathVariable Long id, @RequestBody StudentDTO studentDTO) {
        return ResponseEntity.ok(APIResponse.success(studentService.edit(id, studentDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.ok(APIResponse.success());
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<StudentDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(APIResponse.success(studentService.findById(id)));
    }

    @GetMapping("/registry-topic")
    @ApiOperation(value = "API sinh viên đăng ký đề tài")
    public ResponseEntity<APIResponse<Void>> registryTopic(@RequestParam Long topicId, @RequestParam boolean registry) {
        studentService.studentRegistryTopic(topicId, registry);
        return ResponseEntity.ok(APIResponse.success());
    }

    @GetMapping("/get-list-topic-registry")
    @ApiOperation(value = "API lấy danh sách đề tài sinh viên đã đăng ký")
    public ResponseEntity<APIResponse<PageDataResponse<TopicDTO>>> getListRegistryTopic(SearchTopicStudentRequest request) {
        return ResponseEntity.ok(APIResponse.success(studentService.getTopicOfStudentApproved(request)));
    }

    @GetMapping("/get-list-topic-approved")
    @ApiOperation(value = "API lấy danh sách đề tài sinh viên đã đăng ký đã được duyệt")
    public ResponseEntity<APIResponse<PageDataResponse<TopicDTO>>> getListRegistryTopicApproved(SearchTopicStudentRequest request) {
        return ResponseEntity.ok(APIResponse.success(studentService.getListTopicRegistry(request)));
    }

    @PostMapping("/create-topic-suggest")
    @ApiOperation(value = "API sinh viên đề xuất đề tài")
    public ResponseEntity<APIResponse<Void>> createTopicSuggest(@RequestBody TopicSuggestDTO topicSuggestDTO) {
        studentService.studentSuggestTopic(topicSuggestDTO.getTopicName());
        return ResponseEntity.ok(APIResponse.success());
    }

    @GetMapping("/admin-approve-topic")
    @ApiOperation(value = "API quản trị viên duyệt đề tài sinh viên đề xuất")
    public ResponseEntity<APIResponse<Void>> adminApproveTopicSuggest(@RequestParam Long topicId) {
        studentService.adminApproveTopicSuggest(topicId);
        return ResponseEntity.ok(APIResponse.success());
    }

    @GetMapping("/get-list-topic-suggest")
    @ApiOperation(value = "API lấy danh sách đề tài đề xuất của sinh viên hiện tại")
    public ResponseEntity<APIResponse<PageDataResponse<TopicDTO>>> getListTopicSuggest(SearchTopicStudentRequest request) {
        return ResponseEntity.ok(APIResponse.success(studentService.getListTopicSuggestOfStudent(request)));
    }

    @GetMapping
    public ResponseEntity<APIResponse<PageDataResponse<StudentDTO>>> getAll(SearchStudentRequest request) {
        return ResponseEntity.ok(APIResponse.success(studentService.getAll(request)));
    }
}
