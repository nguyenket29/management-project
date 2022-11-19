package com.hau.ketnguyen.it.service;

import com.hau.ketnguyen.it.model.response.PageDataResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface GenericService<DTO, SR> {
    DTO save(@RequestBody DTO dto);

    DTO edit(@PathVariable Long id, @RequestBody DTO dto);

    void delete(@PathVariable Long id);

    DTO findById(@PathVariable Long id);

    PageDataResponse<DTO> getAll(SR request);
}
