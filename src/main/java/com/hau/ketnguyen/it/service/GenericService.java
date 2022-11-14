package com.hau.ketnguyen.it.service;

import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public abstract class GenericService<DTO, SR> {
    public abstract ResponseEntity<APIResponse<DTO>> save(@RequestBody DTO dto);

    public abstract ResponseEntity<APIResponse<DTO>> edit(@PathVariable Long id, @RequestBody DTO dto);

    public abstract ResponseEntity<APIResponse<Void>> delete(@PathVariable Long id);

    public abstract ResponseEntity<APIResponse<DTO>> findById(@PathVariable Long id);

    public abstract ResponseEntity<APIResponse<PageDataResponse<DTO>>> getAll(SR request);
}
