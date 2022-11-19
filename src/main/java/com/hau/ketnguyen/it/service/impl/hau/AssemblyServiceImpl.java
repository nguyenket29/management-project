package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.model.dto.hau.AssemblyDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchAssemblyRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.AssemblyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AssemblyServiceImpl implements AssemblyService {
    @Override
    public AssemblyDTO save(AssemblyDTO assemblyDTO) {
        return null;
    }

    @Override
    public AssemblyDTO edit(Long id, AssemblyDTO assemblyDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public AssemblyDTO findById(Long id) {
        return null;
    }

    @Override
    public PageDataResponse<AssemblyDTO> getAll(SearchAssemblyRequest request) {
        return null;
    }
}
