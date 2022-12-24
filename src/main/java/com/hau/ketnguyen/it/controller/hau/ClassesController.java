package com.hau.ketnguyen.it.controller.hau;

import com.hau.ketnguyen.it.controller.APIController;
import com.hau.ketnguyen.it.model.dto.hau.ClassDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchClassRequest;
import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.ClassService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "Classes Controller", description = "Các APIs quản lý lớp")
@RequestMapping("/classes")
@AllArgsConstructor
@Slf4j
public class ClassesController {
    private final ClassService classService;

    @PostMapping
    public ResponseEntity<APIResponse<ClassDTO>> save(@RequestBody ClassDTO classDTO) {
        return ResponseEntity.ok(APIResponse.success(classService.save(classDTO)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<ClassDTO>> edit(@PathVariable Long id,@RequestBody ClassDTO classDTO) {
        return ResponseEntity.ok(APIResponse.success(classService.edit(id, classDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> delete(@PathVariable Long id) {
        classService.delete(id);
        return ResponseEntity.ok(APIResponse.success());
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<ClassDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(APIResponse.success(classService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<APIResponse<PageDataResponse<ClassDTO>>> getAll(SearchClassRequest request) {
        return ResponseEntity.ok(APIResponse.success(classService.getAll(request)));
    }
}
