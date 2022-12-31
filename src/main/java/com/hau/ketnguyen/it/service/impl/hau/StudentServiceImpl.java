package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.BeanUtil;
import com.hau.ketnguyen.it.common.util.BeanUtils;
import com.hau.ketnguyen.it.common.util.PageableUtils;
import com.hau.ketnguyen.it.entity.hau.Students;
import com.hau.ketnguyen.it.entity.hau.UserInfo;
import com.hau.ketnguyen.it.model.dto.auth.UserInfoDTO;
import com.hau.ketnguyen.it.model.dto.hau.ClassDTO;
import com.hau.ketnguyen.it.model.dto.hau.StudentDTO;
import com.hau.ketnguyen.it.model.dto.hau.TopicDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchStudentRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.repository.auth.UserInfoReps;
import com.hau.ketnguyen.it.repository.hau.ClassReps;
import com.hau.ketnguyen.it.repository.hau.StudentReps;
import com.hau.ketnguyen.it.repository.hau.TopicReps;
import com.hau.ketnguyen.it.service.StudentService;
import com.hau.ketnguyen.it.service.mapper.ClassMapper;
import com.hau.ketnguyen.it.service.mapper.StudentMapper;
import com.hau.ketnguyen.it.service.mapper.TopicMapper;
import com.hau.ketnguyen.it.service.mapper.UserInfoMapper;
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

@Service
@AllArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {
    private final StudentReps studentReps;
    private final StudentMapper studentMapper;
    private final ClassReps classReps;
    private final TopicReps topicReps;
    private final UserInfoReps userInfoReps;
    private final ClassMapper classMapper;
    private final TopicMapper topicMapper;
    private final UserInfoMapper userInfoMapper;

    @Override
    public StudentDTO save(StudentDTO studentDTO) {
        UserInfo userInfo = setUserInfo(studentDTO);
        userInfoReps.save(userInfo);
        studentDTO.setUserInfoId(userInfo.getId());
        Students students = studentReps.save(studentMapper.from(studentDTO));
        return studentMapper.to(students);
    }

    @Override
    public StudentDTO edit(Long id, StudentDTO studentDTO) {
        Optional<Students> studentOptional = studentReps.findById(id);

        if (studentOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy sinh viên");
        }

        Students students = studentOptional.get();
        Long userInfoId = students.getUserInfoId();
        BeanUtil.copyNonNullProperties(studentDTO, students);
        studentReps.save(students);

        Optional<UserInfo> userInfoOptional = userInfoReps.findById(userInfoId);

        if (userInfoOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy thông tin sinh viên");
        }

        UserInfo userInfo = setUserInfo(studentDTO);
        BeanUtil.copyNonNullProperties(userInfo, userInfoOptional);
        userInfoReps.save(userInfoOptional.get());

        return studentMapper.to(students);
    }

    private UserInfo setUserInfo(StudentDTO studentDTO) {
        UserInfo userInfo = new UserInfo();
        userInfo.setAvatar(studentDTO.getAvatar());
        userInfo.setFullName(studentDTO.getFullName());
        userInfo.setGender(studentDTO.getGender());
        userInfo.setAddress(studentDTO.getAddress());
        userInfo.setTown(studentDTO.getTown());
        userInfo.setDateOfBirth(studentDTO.getDateOfBirth());
        userInfo.setPhoneNumber(studentDTO.getPhoneNumber());
        return userInfo;
    }

    @Override
    public void delete(Long id) {
        Optional<Students> studentOptional = studentReps.findById(id);

        if (studentOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy sinh viên");
        }

        Optional<UserInfo> userInfo = userInfoReps.findById(studentOptional.get().getUserInfoId());
        if (userInfo.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy thông tin giảng viên");
        }

        studentReps.delete(studentOptional.get());
        userInfoReps.delete(userInfo.get());
    }

    @Override
    public StudentDTO findById(Long id) {
        Optional<Students> studentOptional = studentReps.findById(id);

        if (studentOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy sinh viên");
        }

        UserInfoDTO userInfoDTO = userInfoReps.findById(studentOptional.get().getUserInfoId())
                .map(userInfoMapper::to).orElseThrow(() ->
                        APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy thông tin người dùng"));
        ClassDTO classDTO = classReps.findById(studentOptional.get().getClassId())
                .map(classMapper::to).orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy lớp"));
        TopicDTO topicDTO = topicReps.findById(studentOptional.get().getTopicId())
                .map(topicMapper::to).orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy đề tài"));

        StudentDTO studentDTO = studentMapper.to(studentOptional.get());
        studentDTO.setClassDTO(classDTO);
        studentDTO.setUserInfoDTO(userInfoDTO);
        studentDTO.setTopicDTO(topicDTO);
        return studentDTO;
    }

    @Override
    public PageDataResponse<StudentDTO> getAll(SearchStudentRequest request) {
        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        Page<StudentDTO> page = studentReps.search(request, pageable).map(studentMapper::to);

        if (!page.isEmpty()) {
            List<Long> topicIds = page.map(StudentDTO::getTopicId).toList();
            List<Long> classIds = page.map(StudentDTO::getClassId).toList();
            List<Long> userInfoIds = page.map(StudentDTO::getUserInfoId).toList();

            Map<Long, UserInfoDTO> userDTOMap = userInfoReps.findByIdIn(userInfoIds).stream()
                    .map(userInfoMapper::to).collect(Collectors.toMap(UserInfoDTO::getId, u -> u));
            Map<Long, ClassDTO> classDTOMap = classReps.findByIdIn(classIds).stream()
                    .map(classMapper::to).collect(Collectors.toMap(ClassDTO::getId, u -> u));
            Map<Long, TopicDTO> topicDTOMap = topicReps.findByIdIn(topicIds).stream()
                    .map(topicMapper::to).collect(Collectors.toMap(TopicDTO::getId, u -> u));

            page.forEach(p -> {
                if (!userDTOMap.isEmpty() && userDTOMap.containsKey(p.getUserInfoId())) {
                    p.setUserInfoDTO(userDTOMap.get(p.getUserInfoId()));
                }

                if (!classDTOMap.isEmpty() && classDTOMap.containsKey(p.getClassId())) {
                    p.setClassDTO(classDTOMap.get(p.getClassId()));
                }

                if (!topicDTOMap.isEmpty() && topicDTOMap.containsKey(p.getTopicId())) {
                    p.setTopicDTO(topicDTOMap.get(p.getTopicId()));
                }
            });
        }

        return PageDataResponse.of(page);
    }
}
