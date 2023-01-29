package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.BeanUtil;
import com.hau.ketnguyen.it.common.util.PageableUtils;
import com.hau.ketnguyen.it.entity.auth.CustomUser;
import com.hau.ketnguyen.it.entity.hau.*;
import com.hau.ketnguyen.it.model.dto.auth.UserInfoDTO;
import com.hau.ketnguyen.it.model.dto.hau.FacultyDTO;
import com.hau.ketnguyen.it.model.dto.hau.LecturerDTO;
import com.hau.ketnguyen.it.model.dto.hau.StudentTopicDTO;
import com.hau.ketnguyen.it.model.dto.hau.TopicDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchLecturerRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchStudentTopicRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchTopicRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.repository.auth.UserInfoReps;
import com.hau.ketnguyen.it.repository.hau.*;
import com.hau.ketnguyen.it.service.LecturerService;
import com.hau.ketnguyen.it.service.TopicService;
import com.hau.ketnguyen.it.service.mapper.FacultyMapper;
import com.hau.ketnguyen.it.service.mapper.LecturerMapper;
import com.hau.ketnguyen.it.service.mapper.StudentTopicMapper;
import com.hau.ketnguyen.it.service.mapper.UserInfoMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
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
    private final TopicReps topicReps;
    private final TopicService topicService;
    private final StudentTopicReps studentTopicReps;
    private final StudentTopicMapper studentTopicMapper;
    private final StudentReps studentReps;

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
        setLectureRequest(request);
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

    private void setLectureRequest(SearchLecturerRequest request) {
        if (request.getCodeLecture() != null) {
            request.setCodeLecture(request.getCodeLecture().toLowerCase());
        }

        if (request.getAddress() != null) {
            request.setAddress(request.getAddress().toLowerCase());
        }

        if (request.getEmail() != null) {
            request.setEmail(request.getEmail().toLowerCase());
        }

        if (request.getDegree() != null) {
            request.setDegree(request.getDegree().toLowerCase());
        }

        if (request.getTown() != null) {
            request.setTown(request.getTown().toLowerCase());
        }

        if (request.getRegency() != null) {
            request.setRegency(request.getRegency().toLowerCase());
        }

        if (request.getFullName() != null) {
            request.setFullName(request.getFullName().toLowerCase());
        }

        if (request.getPhoneNumber() != null) {
            request.setPhoneNumber(request.getPhoneNumber().toLowerCase());
        }
    }

    @Override
    public PageDataResponse<TopicDTO> getListTopicCounterArgument(SearchTopicRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser user = (CustomUser) authentication.getPrincipal();

        Optional<Lecturers> lecturersOptional = lecturerReps.findByUserId(user.getId());
        if (lecturersOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy giảng viên hướng dẫn");
        }

        request.setLecturerCounterArgumentId(lecturersOptional.get().getId());
        return topicService.getAll(request);
    }

    @Override
    public PageDataResponse<TopicDTO> getListTopicGuide(SearchTopicRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser user = (CustomUser) authentication.getPrincipal();

        Optional<Lecturers> lecturersOptional = lecturerReps.findByUserId(user.getId());
        if (lecturersOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy giảng viên hướng dẫn");
        }

        request.setLecturerGuideId(lecturersOptional.get().getId());
        return topicService.getAll(request);
    }

    @Override
    public PageDataResponse<StudentTopicDTO> getListStudentRegistryTopic(SearchStudentTopicRequest request) {
        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser user = (CustomUser) authentication.getPrincipal();

        Optional<Lecturers> lecturersOptional = lecturerReps.findByUserId(user.getId());
        if (lecturersOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy giảng viên hướng dẫn");
        }

        List<Topics> topics = topicReps.findByLecturerGuideIdIn(Collections.singletonList(lecturersOptional.get().getId()));

        List<Long> topicIds = new ArrayList<>();
        Map<Long, String> mapTopicName = new HashMap<>();
        if (!CollectionUtils.isEmpty(topics)) {
            topicIds = topics.stream().map(Topics::getId).collect(Collectors.toList());
            mapTopicName = topics.stream().collect(Collectors.toMap(Topics::getId, Topics::getName));
            request.setTopicIds(topicIds);
        }

        Page<StudentTopicDTO> studentTopics = studentTopicReps.search(request, pageable).map(studentTopicMapper::to);
        if (!CollectionUtils.isEmpty(studentTopics.toList())) {
            List<Long> studentIds = studentTopics.stream().map(StudentTopicDTO::getStudentId).collect(Collectors.toList());
            List<Students> students = studentReps.findByIdIn(studentIds);

            Map<Long, String> mapStudentName = new HashMap<>();
            if (!CollectionUtils.isEmpty(students)) {
                List<Long> userInfoIds = students.stream().map(Students::getUserInfoId).collect(Collectors.toList());
                Map<Long, String> mapUserInfoIdWithName = userInfoReps.findByIdIn(userInfoIds)
                        .stream().collect(Collectors.toMap(UserInfo::getId, UserInfo::getFullName));
                if (!CollectionUtils.isEmpty(mapUserInfoIdWithName)) {
                    students.forEach(s -> {
                        if (mapUserInfoIdWithName.containsKey(s.getUserInfoId())) {
                            mapStudentName.put(s.getId(), mapUserInfoIdWithName.get(s.getUserInfoId()));
                        }
                    });
                }
            }

            for (StudentTopicDTO s : studentTopics) {
                if (!CollectionUtils.isEmpty(mapTopicName) && mapTopicName.containsKey(s.getTopicId())) {
                    s.setTopicName(mapTopicName.get(s.getTopicId()));
                }

                if (!CollectionUtils.isEmpty(mapStudentName) && mapStudentName.containsKey(s.getStudentId())) {
                    s.setStudentName(mapStudentName.get(s.getStudentId()));
                }
            }
        }

        return PageDataResponse.of(studentTopics);
    }

    @Override
    public void approveTopicForStudent(Long topicId, Long studentId) {
        // cập nhật lại danh sách đăng ký đề tài
        Optional<StudentTopic> studentTopicOptional = studentTopicReps.findByStudentIdAndTopicId(studentId, topicId);
        if (studentTopicOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Sinh viên chưa đăng ký đề tài này");
        }
        studentTopicOptional.get().setStatusApprove(true);

        // cập nhật lại danh sách đề tài
        Optional<Topics> topicsOptional = topicReps.findById(topicId);
        if (topicsOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy đề tài");
        }
        topicsOptional.get().setStatus(true);

        // cập nhật lại sinh viên đã đăng ký đề tài
        Optional<Students> studentOptional = studentReps.findById(studentId);
        if (studentOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy sinh viên");
        }
        studentOptional.get().setTopicId(topicsOptional.get().getId());

        studentTopicReps.save(studentTopicOptional.get());
        topicReps.save(topicsOptional.get());
        studentReps.save(studentOptional.get());
    }
}
