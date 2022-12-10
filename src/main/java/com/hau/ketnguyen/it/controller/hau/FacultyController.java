package com.hau.ketnguyen.it.controller.hau;

import com.hau.ketnguyen.it.controller.APIController;
import com.hau.ketnguyen.it.model.dto.hau.CommentDTO;
import com.hau.ketnguyen.it.model.dto.hau.FacultyDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchCommentRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchFacultyRequest;
import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.CommentService;
import com.hau.ketnguyen.it.service.FacultyService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Faculty Controller", description = "Các APIs quản lý khoa")
@RequestMapping("/faculties")
@AllArgsConstructor
@Slf4j
public class FacultyController extends APIController<FacultyDTO, SearchFacultyRequest> {
    private final FacultyService facultyService;

    @Override
    public ResponseEntity<APIResponse<FacultyDTO>> save(@RequestBody FacultyDTO facultyDTO) {
        return ResponseEntity.ok(APIResponse.success(facultyService.save(facultyDTO)));
    }

    @Override
    public ResponseEntity<APIResponse<FacultyDTO>> edit(Long id,@RequestBody FacultyDTO facultyDTO) {
        return ResponseEntity.ok(APIResponse.success(facultyService.edit(id, facultyDTO)));
    }

    @Override
    public ResponseEntity<APIResponse<Void>> delete(Long id) {
        facultyService.delete(id);
        return ResponseEntity.ok(APIResponse.success());
    }

    @Override
    public ResponseEntity<APIResponse<FacultyDTO>> findById(Long id) {
        return ResponseEntity.ok(APIResponse.success(facultyService.findById(id)));
    }

    @Override
    public ResponseEntity<APIResponse<PageDataResponse<FacultyDTO>>> getAll(SearchFacultyRequest request) {
        return ResponseEntity.ok(APIResponse.success(facultyService.getAll(request)));
    }
}
