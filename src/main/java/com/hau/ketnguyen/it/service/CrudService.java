package com.hau.ketnguyen.it.service;

import com.hau.ketnguyen.it.model.response.PageDataResponse;

public interface CrudService<E, DTO, SRQ> {
    DTO save(DTO dto);

    DTO edit(Long id, DTO dto);

    void delete(Long id);

    DTO findById(Long id);

    PageDataResponse<DTO> findAll(SRQ request);
}
