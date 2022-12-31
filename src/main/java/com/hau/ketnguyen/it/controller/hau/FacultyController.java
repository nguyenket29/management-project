package com.hau.ketnguyen.it.controller.hau;

import com.hau.ketnguyen.it.model.dto.hau.FacultyDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchFacultyRequest;
import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.FacultyService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "Faculty Controller", description = "Các APIs quản lý khoa")
@RequestMapping("/faculties")
@AllArgsConstructor
@Slf4j
public class FacultyController {
    private final FacultyService facultyService;

    @PostMapping
    public ResponseEntity<APIResponse<FacultyDTO>> save(@RequestBody FacultyDTO facultyDTO) {
        return ResponseEntity.ok(APIResponse.success(facultyService.save(facultyDTO)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<FacultyDTO>> edit(@PathVariable Long id,@RequestBody FacultyDTO facultyDTO) {
        return ResponseEntity.ok(APIResponse.success(facultyService.edit(id, facultyDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> delete(@PathVariable Long id) {
        facultyService.delete(id);
        return ResponseEntity.ok(APIResponse.success());
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<FacultyDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(APIResponse.success(facultyService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<APIResponse<PageDataResponse<FacultyDTO>>> getAll(SearchFacultyRequest request) {
        return ResponseEntity.ok(APIResponse.success(facultyService.getAll(request)));
    }
}
