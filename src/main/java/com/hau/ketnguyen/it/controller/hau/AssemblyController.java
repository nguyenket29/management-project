package com.hau.ketnguyen.it.controller.hau;

import com.hau.ketnguyen.it.controller.APIController;
import com.hau.ketnguyen.it.model.dto.hau.AssemblyDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchAssemblyRequest;
import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.AssemblyService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Assembly Controller", description = "Các APIs quản lý hội đồng")
@RequestMapping("/assemblies")
@AllArgsConstructor
@Slf4j
public class AssemblyController extends APIController<AssemblyDTO, SearchAssemblyRequest> {
    private final AssemblyService assemblyService;

    @Override
    public ResponseEntity<APIResponse<AssemblyDTO>> save(@RequestBody AssemblyDTO assemblyDTO) {
        return ResponseEntity.ok(APIResponse.success(assemblyService.save(assemblyDTO)));
    }

    @Override
    public ResponseEntity<APIResponse<AssemblyDTO>> edit(Long id,@RequestBody AssemblyDTO assemblyDTO) {
        return ResponseEntity.ok(APIResponse.success(assemblyService.edit(id, assemblyDTO)));
    }

    @Override
    public ResponseEntity<APIResponse<Void>> delete(Long id) {
        assemblyService.delete(id);
        return ResponseEntity.ok(APIResponse.success());
    }

    @Override
    public ResponseEntity<APIResponse<AssemblyDTO>> findById(Long id) {
        return ResponseEntity.ok(APIResponse.success(assemblyService.findById(id)));
    }

    @Override
    public ResponseEntity<APIResponse<PageDataResponse<AssemblyDTO>>> getAll(SearchAssemblyRequest request) {
        return ResponseEntity.ok(APIResponse.success(assemblyService.getAll(request)));
    }
}
