package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.BeanUtil;
import com.hau.ketnguyen.it.common.util.PageableUtils;
import com.hau.ketnguyen.it.entity.hau.Lecturers;
import com.hau.ketnguyen.it.entity.hau.UserInfo;
import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import com.hau.ketnguyen.it.model.dto.auth.UserInfoDTO;
import com.hau.ketnguyen.it.model.dto.hau.FacultyDTO;
import com.hau.ketnguyen.it.model.dto.hau.LecturerDTO;
import com.hau.ketnguyen.it.model.dto.hau.WorkplaceDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchLecturerRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.repository.auth.UserInfoReps;
import com.hau.ketnguyen.it.repository.auth.UserReps;
import com.hau.ketnguyen.it.repository.hau.FacultyReps;
import com.hau.ketnguyen.it.repository.hau.LecturerReps;
import com.hau.ketnguyen.it.repository.hau.WorkplaceReps;
import com.hau.ketnguyen.it.service.LecturerService;
import com.hau.ketnguyen.it.service.mapper.*;
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
    private final FacultyReps facultyReps;
    private final FacultyMapper facultyMapper;
    private final UserInfoReps userInfoReps;
    private final UserInfoMapper userInfoMapper;

    @Override
    public LecturerDTO save(LecturerDTO lecturerDTO) {
        UserInfo userInfo = setUserInfo(lecturerDTO);
        userInfoReps.save(userInfo);
        lecturerDTO.setUserInfoId(userInfo.getId());
        Lecturers lecturers = lecturerReps.save(lecturerMapper.from(lecturerDTO));
        return lecturerMapper.to(lecturers);
    }

    @Override
    public LecturerDTO edit(Long id, LecturerDTO lecturerDTO) {
        Optional<Lecturers> lecturersOptional = lecturerReps.findById(id);

        if (lecturersOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy giảng viên");
        }

        Lecturers lecturers = lecturersOptional.get();
        Long userInfoId = lecturers.getUserInfoId();
        BeanUtil.copyNonNullProperties(lecturerDTO, lecturers);
        lecturerReps.save(lecturers);

        Optional<UserInfo> userInfoOptional = userInfoReps.findById(userInfoId);

        if (userInfoOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy thông tin sinh viên");
        }

        UserInfo userInfo = setUserInfo(lecturerDTO);
        BeanUtil.copyNonNullProperties(userInfo, userInfoOptional.get());
        userInfoReps.save(userInfoOptional.get());

        return lecturerMapper.to(lecturers);
    }

    private UserInfo setUserInfo(LecturerDTO lecturerDTO) {
        UserInfo userInfo = new UserInfo();
        userInfo.setAvatar(lecturerDTO.getAvatar());
        userInfo.setFullName(lecturerDTO.getFullName());
        userInfo.setGender(lecturerDTO.getGender());
        userInfo.setAddress(lecturerDTO.getAddress());
        userInfo.setTown(lecturerDTO.getTown());
        userInfo.setDateOfBirth(lecturerDTO.getDateOfBirth());
        userInfo.setPhoneNumber(lecturerDTO.getPhoneNumber());
        return userInfo;
    }

    @Override
    public void delete(Long id) {
        Optional<Lecturers> lecturersOptional = lecturerReps.findById(id);

        if (lecturersOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy giảng viên");
        }

        Optional<UserInfo> userInfo = userInfoReps.findById(lecturersOptional.get().getUserInfoId());
        if (userInfo.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy thông tin giảng viên");
        }

        userInfoReps.delete(userInfo.get());
        lecturerReps.delete(lecturersOptional.get());
    }

    @Override
    public LecturerDTO findById(Long id) {
        Optional<Lecturers> lecturersOptional = lecturerReps.findById(id);

        if (lecturersOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy giảng viên");
        }

        UserInfoDTO userInfoDTO = userInfoReps.findById(lecturersOptional.get().getUserInfoId())
                .map(userInfoMapper::to).orElseThrow(() ->
                        APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy thông tin người dùng"));
        FacultyDTO facultyDTO = facultyReps.findById(lecturersOptional.get().getFacultyId())
                .map(facultyMapper::to).orElseThrow(() ->
                        APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy khoa"));

        LecturerDTO lecturerDTO = lecturerMapper.to(lecturersOptional.get());
        lecturerDTO.setUserInfoDTO(userInfoDTO);
        lecturerDTO.setFacultyDTO(facultyDTO);

        return lecturerDTO;
    }

    @Override
    public PageDataResponse<LecturerDTO> getAll(SearchLecturerRequest request) {
        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        Page<LecturerDTO> page = lecturerReps.search(request, pageable).map(lecturerMapper::to);

        if (!page.isEmpty()) {
            List<Long> userInfoIds = page.map(LecturerDTO::getUserInfoId).toList();
            List<Long> facultyIds = page.map(LecturerDTO::getFacultyId).toList();

            Map<Long, UserInfoDTO> userInfoDTOMap = userInfoReps.findByIdIn(userInfoIds).stream()
                    .map(userInfoMapper::to).collect(Collectors.toMap(UserInfoDTO::getId, u -> u));
            Map<Long, FacultyDTO> facultyDTOMap = facultyReps.findByIdIn(facultyIds).stream()
                    .map(facultyMapper::to).collect(Collectors.toMap(FacultyDTO::getId, u -> u));

            page.forEach(p -> {
                if (!userInfoDTOMap.isEmpty() && userInfoDTOMap.containsKey(p.getUserInfoId())) {
                    p.setUserInfoDTO(userInfoDTOMap.get(p.getUserInfoId()));
                }

                if (!facultyDTOMap.isEmpty() && facultyDTOMap.containsKey(p.getFacultyId())) {
                    p.setFacultyDTO(facultyDTOMap.get(p.getFacultyId()));
                }
            });
        }

        return PageDataResponse.of(page);
    }
}
