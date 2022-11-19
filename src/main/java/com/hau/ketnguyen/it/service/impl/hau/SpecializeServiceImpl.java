package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.model.dto.hau.SpecializeDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchSpecializeRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.SpecializeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class SpecializeServiceImpl implements SpecializeService {
    @Override
    public SpecializeDTO save(SpecializeDTO specializeDTO) {
        return null;
    }

    @Override
    public SpecializeDTO edit(Long id, SpecializeDTO specializeDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public SpecializeDTO findById(Long id) {
        return null;
    }

    @Override
    public PageDataResponse<SpecializeDTO> getAll(SearchSpecializeRequest request) {
        return null;
    }
}
