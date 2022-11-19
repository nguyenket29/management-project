package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.model.dto.hau.LecturerDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchLecturerRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.LecturerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class LecturerServiceImpl implements LecturerService {
    @Override
    public LecturerDTO save(LecturerDTO lecturerDTO) {
        return null;
    }

    @Override
    public LecturerDTO edit(Long id, LecturerDTO lecturerDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public LecturerDTO findById(Long id) {
        return null;
    }

    @Override
    public PageDataResponse<LecturerDTO> getAll(SearchLecturerRequest request) {
        return null;
    }
}
