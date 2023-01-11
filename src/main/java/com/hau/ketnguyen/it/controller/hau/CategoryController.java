package com.hau.ketnguyen.it.controller.hau;

import com.hau.ketnguyen.it.model.dto.hau.CategoryDTO;
import com.hau.ketnguyen.it.model.dto.hau.ClassDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchCategoryRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchClassRequest;
import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.CategoryService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "Categories Controller", description = "Các APIs quản lý chủ đề đề tài")
@RequestMapping("/categories")
@AllArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<APIResponse<CategoryDTO>> save(@RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(APIResponse.success(categoryService.save(categoryDTO)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<CategoryDTO>> edit(@PathVariable Long id,@RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(APIResponse.success(categoryService.edit(id, categoryDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(APIResponse.success());
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<CategoryDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(APIResponse.success(categoryService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<APIResponse<PageDataResponse<CategoryDTO>>> getAll(SearchCategoryRequest request) {
        return ResponseEntity.ok(APIResponse.success(categoryService.getAll(request)));
    }
}
