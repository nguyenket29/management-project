package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.PageableUtils;
import com.hau.ketnguyen.it.entity.hau.Faculties;
import com.hau.ketnguyen.it.entity.hau.Specialize;
import com.hau.ketnguyen.it.entity.hau.TrainingSystem;
import com.hau.ketnguyen.it.model.dto.hau.*;
import com.hau.ketnguyen.it.model.request.hau.SearchSpecializeRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.repository.hau.FacultyReps;
import com.hau.ketnguyen.it.repository.hau.SpecializeReps;
import com.hau.ketnguyen.it.repository.hau.TrainingSystemReps;
import com.hau.ketnguyen.it.service.SpecializeService;
import com.hau.ketnguyen.it.service.mapper.FacultyMapper;
import com.hau.ketnguyen.it.service.mapper.SpecializeMapper;
import com.hau.ketnguyen.it.service.mapper.TrainingSystemMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class SpecializeServiceImpl implements SpecializeService {
    private final SpecializeMapper specializeMapper;
    private final SpecializeReps specializeReps;
    private final FacultyReps facultyReps;
    private final FacultyMapper facultyMapper;
    private final TrainingSystemReps trainingSystemReps;
    private final TrainingSystemMapper trainingSystemMapper;

    @Override
    public SpecializeDTO save(SpecializeDTO specializeDTO) {
        return specializeMapper.to(specializeReps.save(specializeMapper.from(specializeDTO)));
    }

    @Override
    public SpecializeDTO edit(Long id, SpecializeDTO specializeDTO) {
        Optional<Specialize> specializeOptional = specializeReps.findById(id);

        if (specializeOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy chuyên ngành");
        }

        Specialize specialize = specializeOptional.get();
        specializeMapper.copy(specializeDTO, specialize);

        return specializeMapper.to(specialize);
    }

    @Override
    public void delete(Long id) {
        Optional<Specialize> specializeOptional = specializeReps.findById(id);

        if (specializeOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy chuyên ngành");
        }

        specializeReps.delete(specializeOptional.get());
    }

    @Override
    public SpecializeDTO findById(Long id) {
        Optional<Specialize> specializeOptional = specializeReps.findById(id);

        if (specializeOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy chuyên ngành");
        }

        Faculties faculties = facultyReps.findById(specializeOptional.get().getFacultyId())
                .orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy khoa"));
        FacultyDTO facultyDTO = facultyMapper.to(faculties);
        TrainingSystem trainingSystem = trainingSystemReps.findById(specializeOptional.get().getTrainSystemId())
                .orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy hệ đào tạo"));
        TrainingSystemDTO trainingSystemDTO = trainingSystemMapper.to(trainingSystem);

        SpecializeDTO specializeDTO = specializeMapper.to(specializeOptional.get());
        specializeDTO.setFacultyDTO(facultyDTO);
        specializeDTO.setTrainingSystemDTO(trainingSystemDTO);

        return specializeDTO;
    }

    @Override
    public PageDataResponse<SpecializeDTO> getAll(SearchSpecializeRequest request) {
        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        Page<SpecializeDTO> page = specializeReps.search(request, pageable).map(specializeMapper::to);

        if (!page.isEmpty()) {
            List<Long> facultyIds = page.map(SpecializeDTO::getFacultyId).toList();
            List<Long> trainIds = page.map(SpecializeDTO::getTrainSystemId).toList();

            Map<Long, FacultyDTO> facultyDTOS = facultyReps.findByIdIn(facultyIds).stream()
                    .map(facultyMapper::to).collect(Collectors.toMap(FacultyDTO::getId, u -> u));
            Map<Long, TrainingSystemDTO> trainingSystemDTOMap = trainingSystemReps.findByIdIn(trainIds).stream()
                    .map(trainingSystemMapper::to).collect(Collectors.toMap(TrainingSystemDTO::getId, u -> u));

            page.forEach(p -> {
                if (!facultyDTOS.isEmpty() && facultyDTOS.containsKey(p.getFacultyId())) {
                    p.setFacultyDTO(facultyDTOS.get(p.getFacultyId()));
                }

                if (!trainingSystemDTOMap.isEmpty() && trainingSystemDTOMap.containsKey(p.getTrainSystemId())) {
                    p.setTrainingSystemDTO(trainingSystemDTOMap.get(p.getTrainSystemId()));
                }
            });
        }

        return PageDataResponse.of(page);
    }
}
