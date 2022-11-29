package com.hau.ketnguyen.it.controller;

import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public abstract class APIController<DTO, SR> {
    @PostMapping
    public abstract ResponseEntity<APIResponse<DTO>> save(@RequestBody DTO dto);

    @PutMapping("/{id}")
    public abstract ResponseEntity<APIResponse<DTO>> edit(@PathVariable Long id, @RequestBody DTO dto);

    @DeleteMapping("/{id}")
    public abstract ResponseEntity<APIResponse<Void>> delete(@PathVariable Long id);

    @GetMapping("/{id}")
    public abstract ResponseEntity<APIResponse<DTO>> findById(@PathVariable Long id);

    @GetMapping
    public abstract ResponseEntity<APIResponse<PageDataResponse<DTO>>> getAll(SR request);

}
