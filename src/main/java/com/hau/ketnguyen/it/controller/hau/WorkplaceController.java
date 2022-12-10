package com.hau.ketnguyen.it.controller.hau;

import com.hau.ketnguyen.it.controller.APIController;
import com.hau.ketnguyen.it.model.dto.hau.WorkplaceDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchWorkplaceRequest;
import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.WorkplaceService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Workplace Controller", description = "Các APIs quản lý nơi làm việc")
@RequestMapping("/workplaces")
@AllArgsConstructor
@Slf4j
public class WorkplaceController extends APIController<WorkplaceDTO, SearchWorkplaceRequest> {
    private final WorkplaceService workplaceService;

    @Override
    public ResponseEntity<APIResponse<WorkplaceDTO>> save(@RequestBody WorkplaceDTO workplaceDTO) {
        return ResponseEntity.ok(APIResponse.success(workplaceService.save(workplaceDTO)));
    }

    @Override
    public ResponseEntity<APIResponse<WorkplaceDTO>> edit(Long id, @RequestBody WorkplaceDTO workplaceDTO) {
        return ResponseEntity.ok(APIResponse.success(workplaceService.edit(id, workplaceDTO)));
    }

    @Override
    public ResponseEntity<APIResponse<Void>> delete(Long id) {
        workplaceService.delete(id);
        return ResponseEntity.ok(APIResponse.success());
    }

    @Override
    public ResponseEntity<APIResponse<WorkplaceDTO>> findById(Long id) {
        return ResponseEntity.ok(APIResponse.success(workplaceService.findById(id)));
    }

    @Override
    public ResponseEntity<APIResponse<PageDataResponse<WorkplaceDTO>>> getAll(SearchWorkplaceRequest request) {
        return ResponseEntity.ok(APIResponse.success(workplaceService.getAll(request)));
    }
}
