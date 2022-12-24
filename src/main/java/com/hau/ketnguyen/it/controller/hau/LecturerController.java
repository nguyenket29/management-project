package com.hau.ketnguyen.it.controller.hau;

import com.hau.ketnguyen.it.model.dto.hau.LecturerDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchLecturerRequest;
import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.LecturerService;
import io.swagger.annotations.Api;
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

    @GetMapping
    public ResponseEntity<APIResponse<PageDataResponse<LecturerDTO>>> getAll(SearchLecturerRequest request) {
        return ResponseEntity.ok(APIResponse.success(lecturerService.getAll(request)));
    }
}
