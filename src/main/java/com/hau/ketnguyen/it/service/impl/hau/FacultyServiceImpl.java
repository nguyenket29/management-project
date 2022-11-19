package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.PageableUtils;
import com.hau.ketnguyen.it.entity.hau.Faculties;
import com.hau.ketnguyen.it.model.dto.hau.FacultyDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchFacultyRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.repository.hau.FacultyReps;
import com.hau.ketnguyen.it.service.FacultyService;
import com.hau.ketnguyen.it.service.mapper.FacultyMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class FacultyServiceImpl implements FacultyService {
    private final FacultyReps facultyReps;
    private final FacultyMapper facultyMapper;

    @Override
    public FacultyDTO save(FacultyDTO facultyDTO) {
        return facultyMapper.to(facultyReps.save(facultyMapper.from(facultyDTO)));
    }

    @Override
    public FacultyDTO edit(Long id, FacultyDTO facultyDTO) {
        Faculties facultiesOpt = facultyReps.findById(id).orElseThrow(
                () -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy khoa " + id));
        facultyMapper.copy(facultyDTO, facultiesOpt);
        return facultyMapper.to(facultiesOpt);
    }

    @Override
    public void delete(Long id) {
        Faculties facultiesOpt = facultyReps.findById(id).orElseThrow(
                () -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy khoa " + id));
        facultyReps.delete(facultiesOpt);
    }

    @Override
    public FacultyDTO findById(Long id) {
        Faculties facultiesOpt = facultyReps.findById(id).orElseThrow(
                () -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy khoa " + id));
        return facultyMapper.to(facultiesOpt);
    }

    @Override
    public PageDataResponse<FacultyDTO> getAll(SearchFacultyRequest request) {
        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        Page<FacultyDTO> facultiesPage = facultyReps.search(request, pageable).map(facultyMapper::to);
        return PageDataResponse.of(facultiesPage);
    }
}
