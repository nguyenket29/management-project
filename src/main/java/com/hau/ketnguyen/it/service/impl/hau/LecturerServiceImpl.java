package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.BeanUtil;
import com.hau.ketnguyen.it.common.util.PageableUtils;
import com.hau.ketnguyen.it.entity.hau.Lecturers;
import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import com.hau.ketnguyen.it.model.dto.hau.FacultyDTO;
import com.hau.ketnguyen.it.model.dto.hau.LecturerDTO;
import com.hau.ketnguyen.it.model.dto.hau.WorkplaceDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchLecturerRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.repository.auth.UserReps;
import com.hau.ketnguyen.it.repository.hau.FacultyReps;
import com.hau.ketnguyen.it.repository.hau.LecturerReps;
import com.hau.ketnguyen.it.repository.hau.WorkplaceReps;
import com.hau.ketnguyen.it.service.LecturerService;
import com.hau.ketnguyen.it.service.mapper.FacultyMapper;
import com.hau.ketnguyen.it.service.mapper.LecturerMapper;
import com.hau.ketnguyen.it.service.mapper.UserMapper;
import com.hau.ketnguyen.it.service.mapper.WorkplaceMapper;
import io.swagger.models.auth.In;
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
@Service
@AllArgsConstructor
public class LecturerServiceImpl implements LecturerService {
    private final LecturerMapper lecturerMapper;
    private final LecturerReps lecturerReps;
    private final UserReps userReps;
    private final UserMapper userMapper;
    private final FacultyReps facultyReps;
    private final FacultyMapper facultyMapper;

    @Override
    public LecturerDTO save(LecturerDTO lecturerDTO) {
        return lecturerMapper.to(lecturerReps.save(lecturerMapper.from(lecturerDTO)));
    }

    @Override
    public LecturerDTO edit(Long id, LecturerDTO lecturerDTO) {
        Optional<Lecturers> lecturersOptional = lecturerReps.findById(id);

        if (lecturersOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy giảng viên");
        }

        Lecturers lecturers = lecturersOptional.get();
        BeanUtil.copyNonNullProperties(lecturerDTO, lecturers);

        return lecturerMapper.to(lecturerReps.save(lecturers));
    }

    @Override
    public void delete(Long id) {
        Optional<Lecturers> lecturersOptional = lecturerReps.findById(id);

        if (lecturersOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy giảng viên");
        }

        lecturerReps.delete(lecturersOptional.get());
    }

    @Override
    public LecturerDTO findById(Long id) {
        Optional<Lecturers> lecturersOptional = lecturerReps.findById(id);

        if (lecturersOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy giảng viên");
        }

        UserDTO userDTO = userReps.findById(lecturersOptional.get().getUserId())
                .map(userMapper::to).orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy người dùng"));
        FacultyDTO facultyDTO = facultyReps.findById(lecturersOptional.get().getFacultyId())
                .map(facultyMapper::to).orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy khoa"));

        LecturerDTO lecturerDTO = lecturerMapper.to(lecturersOptional.get());
        lecturerDTO.setUserDTO(userDTO);
        lecturerDTO.setFacultyDTO(facultyDTO);

        return lecturerDTO;
    }

    @Override
    public PageDataResponse<LecturerDTO> getAll(SearchLecturerRequest request) {
        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        Page<LecturerDTO> page = lecturerReps.search(request, pageable).map(lecturerMapper::to);

        if (!page.isEmpty()) {
            List<Integer> userIds = page.map(LecturerDTO::getUserId).toList();
            List<Long> facultyIds = page.map(LecturerDTO::getFacultyId).toList();

            Map<Integer, UserDTO> userDTOMap = userReps.findByIds(userIds).stream()
                    .map(userMapper::to).collect(Collectors.toMap(UserDTO::getId, u -> u));
            Map<Long, FacultyDTO> facultyDTOMap = facultyReps.findByIdIn(facultyIds).stream()
                    .map(facultyMapper::to).collect(Collectors.toMap(FacultyDTO::getId, u -> u));

            page.forEach(p -> {
                if (!userDTOMap.isEmpty() && userDTOMap.containsKey(p.getUserId())) {
                    p.setUserDTO(userDTOMap.get(p.getUserId()));
                }

                if (!facultyDTOMap.isEmpty() && facultyDTOMap.containsKey(p.getFacultyId())) {
                    p.setFacultyDTO(facultyDTOMap.get(p.getFacultyId()));
                }
            });
        }

        return null;
    }
}
