package com.hau.ketnguyen.it.controller.hau;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hau.ketnguyen.it.model.dto.hau.AssemblyDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchAssemblyRequest;
import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.AssemblyService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "Assembly Controller", description = "Các APIs quản lý hội đồng")
@RequestMapping("/assemblies")
@AllArgsConstructor
@Slf4j
public class AssemblyController {
    private final AssemblyService assemblyService;

    @PostMapping
    public ResponseEntity<APIResponse<AssemblyDTO>> save(@RequestBody AssemblyDTO assemblyDTO) throws JsonProcessingException {
        return ResponseEntity.ok(APIResponse.success(assemblyService.save(assemblyDTO)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<AssemblyDTO>> edit(@PathVariable Long id,@RequestBody AssemblyDTO assemblyDTO) throws JsonProcessingException {
        return ResponseEntity.ok(APIResponse.success(assemblyService.edit(id, assemblyDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> delete(@PathVariable Long id) {
        assemblyService.delete(id);
        return ResponseEntity.ok(APIResponse.success());
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<AssemblyDTO>> findById(@PathVariable Long id) throws JsonProcessingException {
        return ResponseEntity.ok(APIResponse.success(assemblyService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<APIResponse<PageDataResponse<AssemblyDTO>>> getAll(SearchAssemblyRequest request) {
        return ResponseEntity.ok(APIResponse.success(assemblyService.getAll(request)));
    }
}
