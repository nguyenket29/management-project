package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.model.dto.hau.StudentDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchStudentRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.StudentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {
    @Override
    public StudentDTO save(StudentDTO studentDTO) {
        return null;
    }

    @Override
    public StudentDTO edit(Long id, StudentDTO studentDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public StudentDTO findById(Long id) {
        return null;
    }

    @Override
    public PageDataResponse<StudentDTO> getAll(SearchStudentRequest request) {
        return null;
    }
}
