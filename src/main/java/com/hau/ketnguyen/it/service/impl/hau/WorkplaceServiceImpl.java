package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.PageableUtils;
import com.hau.ketnguyen.it.entity.hau.Workplaces;
import com.hau.ketnguyen.it.model.dto.hau.WorkplaceDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchWorkplaceRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.repository.hau.WorkplaceReps;
import com.hau.ketnguyen.it.service.WorkplaceService;
import com.hau.ketnguyen.it.service.mapper.WorkplaceMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class WorkplaceServiceImpl implements WorkplaceService {
    private final WorkplaceMapper workplaceMapper;
    private final WorkplaceReps workplaceReps;

    @Override
    public WorkplaceDTO save(WorkplaceDTO workplaceDTO) {
        return workplaceMapper.to(workplaceReps.save(workplaceMapper.from(workplaceDTO)));
    }

    @Override
    public WorkplaceDTO edit(Long id, WorkplaceDTO workplaceDTO) {
        Optional<Workplaces> workplacesOptional = workplaceReps.findById(id);

        if (workplacesOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy đơn vị");
        }

        Workplaces workplaces = workplacesOptional.get();
        workplaceMapper.copy(workplaceDTO, workplaces);
        return workplaceMapper.to(workplaces);
    }

    @Override
    public void delete(Long id) {
        Optional<Workplaces> workplacesOptional = workplaceReps.findById(id);

        if (workplacesOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy đơn vị");
        }

        workplaceReps.delete(workplacesOptional.get());
    }

    @Override
    public WorkplaceDTO findById(Long id) {
        Optional<Workplaces> workplacesOptional = workplaceReps.findById(id);

        if (workplacesOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy đơn vị");
        }

        return workplaceMapper.to(workplacesOptional.get());
    }

    @Override
    public PageDataResponse<WorkplaceDTO> getAll(SearchWorkplaceRequest request) {
        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        Page<WorkplaceDTO> page = workplaceReps.search(request, pageable).map(workplaceMapper::to);
        return PageDataResponse.of(page);
    }
}
