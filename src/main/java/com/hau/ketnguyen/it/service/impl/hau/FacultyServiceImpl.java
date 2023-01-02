package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.BeanUtil;
import com.hau.ketnguyen.it.common.util.PageableUtils;
import com.hau.ketnguyen.it.entity.hau.Faculties;
import com.hau.ketnguyen.it.entity.hau.Workplaces;
import com.hau.ketnguyen.it.model.dto.hau.FacultyDTO;
import com.hau.ketnguyen.it.model.dto.hau.WorkplaceDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchFacultyRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.repository.hau.FacultyReps;
import com.hau.ketnguyen.it.repository.hau.WorkplaceReps;
import com.hau.ketnguyen.it.service.FacultyService;
import com.hau.ketnguyen.it.service.mapper.FacultyMapper;
import com.hau.ketnguyen.it.service.mapper.WorkplaceMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class FacultyServiceImpl implements FacultyService {
    private final FacultyReps facultyReps;
    private final FacultyMapper facultyMapper;
    private final WorkplaceReps workplaceReps;
    private final WorkplaceMapper workplaceMapper;

    @Override
    public FacultyDTO save(FacultyDTO facultyDTO) {
        return facultyMapper.to(facultyReps.save(facultyMapper.from(facultyDTO)));
    }

    @Override
    public FacultyDTO edit(Long id, FacultyDTO facultyDTO) {
        Faculties facultiesOpt = facultyReps.findById(id).orElseThrow(
                () -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy khoa " + id));
        BeanUtil.copyNonNullProperties(facultyDTO, facultiesOpt);
        return facultyMapper.to(facultyReps.save(facultiesOpt));
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
        FacultyDTO facultyDTO = facultyMapper.to(facultiesOpt);
        Workplaces workplaces = workplaceReps.findById(facultiesOpt.getWorkplaceId())
                .orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy đơn vị"));
        WorkplaceDTO workplaceDTO = workplaceMapper.to(workplaces);
        facultyDTO.setWorkplaceDTO(workplaceDTO);
        return facultyDTO;
    }

    @Override
    public PageDataResponse<FacultyDTO> getAll(SearchFacultyRequest request) {
        setFacultyRequest(request);

        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        Page<FacultyDTO> facultiesPage = facultyReps.search(request, pageable).map(facultyMapper::to);

        if (!facultiesPage.isEmpty()) {
            List<Long> workplaceIds = facultiesPage.map(FacultyDTO::getWorkplaceId).toList();
            Map<Long, WorkplaceDTO> workplaceDTOS = workplaceReps.findByIdIn(workplaceIds)
                    .stream().map(workplaceMapper::to).collect(Collectors.toMap(WorkplaceDTO::getId, w -> w));

            if (!workplaceDTOS.isEmpty()) {
                facultiesPage.forEach(f -> {
                    if (workplaceDTOS.containsKey(f.getWorkplaceId())) {
                        f.setWorkplaceDTO(workplaceDTOS.get(f.getWorkplaceId()));
                    }
                });
            }
        }

        return PageDataResponse.of(facultiesPage);
    }

    private void setFacultyRequest(SearchFacultyRequest request) {
        if (request.getName() != null) {
            request.setName(request.getName().toLowerCase());
        }

        if (request.getCode() != null) {
            request.setCode(request.getCode().toLowerCase());
        }

        if (request.getSpecialization() != null) {
            request.setSpecialization(request.getSpecialization().toLowerCase());
        }
    }
}
