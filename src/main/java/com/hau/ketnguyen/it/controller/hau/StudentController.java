package com.hau.ketnguyen.it.controller.hau;

import com.hau.ketnguyen.it.model.dto.hau.StudentDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchStudentRequest;
import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.StudentService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<APIResponse<PageDataResponse<StudentDTO>>> getAll(SearchStudentRequest request) {
        return ResponseEntity.ok(APIResponse.success(studentService.getAll(request)));
    }
}
