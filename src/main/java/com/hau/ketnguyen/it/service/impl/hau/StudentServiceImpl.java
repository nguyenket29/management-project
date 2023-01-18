package com.hau.ketnguyen.it.service.impl.hau;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.BeanUtil;
import com.hau.ketnguyen.it.common.util.PageableUtils;
import com.hau.ketnguyen.it.entity.auth.CustomUser;
import com.hau.ketnguyen.it.entity.hau.*;
import com.hau.ketnguyen.it.model.dto.auth.UserInfoDTO;
import com.hau.ketnguyen.it.model.dto.hau.ClassDTO;
import com.hau.ketnguyen.it.model.dto.hau.LecturerDTO;
import com.hau.ketnguyen.it.model.dto.hau.StudentDTO;
import com.hau.ketnguyen.it.model.dto.hau.TopicDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchStudentRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchTopicStudentRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.repository.auth.UserInfoReps;
import com.hau.ketnguyen.it.repository.hau.*;
import com.hau.ketnguyen.it.service.StudentService;
import com.hau.ketnguyen.it.service.mapper.*;
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

import static com.hau.ketnguyen.it.service.impl.hau.AssemblyServiceImpl.getLongLecturerDTOMap;

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
    private final StudentTopicReps studentTopicReps;
    private final LecturerReps lecturerReps;
    private final LecturerMapper lecturerMapper;
    private final CategoryReps categoryReps;

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
        BeanUtil.copyNonNullProperties(userInfo, userInfoOptional.get());
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
        setStudentRequest(request);
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

    private void setStudentRequest(SearchStudentRequest request) {
        if (request.getCodeStudent() != null) {
            request.setCodeStudent(request.getCodeStudent().toLowerCase());
        }

        if (request.getAddress() != null) {
            request.setAddress(request.getAddress().toLowerCase());
        }

        if (request.getTown() != null) {
            request.setTown(request.getTown().toLowerCase());
        }

        if (request.getEmail() != null) {
            request.setEmail(request.getEmail().toLowerCase());
        }

        if (request.getFullName() != null) {
            request.setFullName(request.getFullName().toLowerCase());
        }

        if (request.getPhoneNumber() != null) {
            request.setPhoneNumber(request.getPhoneNumber().toLowerCase());
        }
    }

    @Override
    public void studentRegistryTopic(Long topicId, boolean registry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser user = (CustomUser) authentication.getPrincipal();

        if (topicId != null) {
            Optional<Students> students = studentReps.findByUserId(user.getId());

            if (students.isEmpty()) {
                throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy sinh viên");
            }

            StudentTopic studentTopic = new StudentTopic();
            studentTopic.setTopicId(topicId);
            studentTopic.setStudentId(students.get().getId());
            studentTopic.setStatus(registry);
            studentTopicReps.save(studentTopic);
        }
    }

    @Override
    public PageDataResponse<TopicDTO> getListTopicRegistry(SearchTopicStudentRequest request) {
        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser user = (CustomUser) authentication.getPrincipal();

        Optional<Students> students = studentReps.findByUserId(user.getId());

        if (students.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy sinh viên");
        }

        List<StudentTopic> studentTopics =
                studentTopicReps.findByStudentIdIn(Collections.singletonList(students.get().getId()));

        Map<Long, Boolean> mapTopicStatusStudentRegistry = studentTopics.stream()
                .collect(Collectors.toMap(StudentTopic::getTopicId, StudentTopic::getStatus));
        List<Long> topicIds = studentTopics.stream().map(StudentTopic::getTopicId).distinct().collect(Collectors.toList());
        request.setTopicIds(topicIds);
        Page<TopicDTO> page = topicReps.getListByTopicIds(request, pageable).map(topicMapper::to);
        setTopicDTO(page, topicIds, mapTopicStatusStudentRegistry);

        return PageDataResponse.of(page);
    }

    private void setTopicDTO(Page<TopicDTO> topicDTOS, List<Long> topicIds, Map<Long, Boolean> mapTopicStatusStudentRegistry) {
        if (!CollectionUtils.isEmpty(topicDTOS.toList())) {
            List<Long> categoryIds = topicDTOS.stream().map(TopicDTO::getCategoryId).collect(Collectors.toList());
            List<Long> lectureGuideIds = topicDTOS.stream().map(TopicDTO::getLecturerGuideId).distinct().collect(Collectors.toList());
            List<Long> lectureCounterArgumentIds = topicDTOS.stream()
                    .map(TopicDTO::getLecturerCounterArgumentId).distinct().collect(Collectors.toList());

            List<Long> lectureIds = new ArrayList<>();
            lectureIds.addAll(lectureGuideIds);
            lectureIds.addAll(lectureCounterArgumentIds);

            Map<Long, LecturerDTO> lecturerDTOMap = setLecture(lectureIds.stream().distinct().collect(Collectors.toList()));
            Map<Long, List<String>> mapFileIds = mapTopicIdWithListFileId(topicIds);
            Map<Long, String> mapCategoryName = mapTopicWithCategoryName(categoryIds);

            topicDTOS.forEach(p -> {
                if (p.getLecturerGuideId() != null && lecturerDTOMap.containsKey(p.getLecturerGuideId())) {
                    p.setLecturerGuideDTO(lecturerDTOMap.get(p.getLecturerGuideId()));
                }

                if (p.getLecturerCounterArgumentId() != null && lecturerDTOMap.containsKey(p.getLecturerCounterArgumentId())) {
                    p.setLecturerCounterArgumentDTO(lecturerDTOMap.get(p.getLecturerCounterArgumentId()));
                }

                if (!mapFileIds.isEmpty() && mapFileIds.containsKey(p.getId())) {
                    p.setFileIds(mapFileIds.get(p.getId()));
                }

                if (!mapCategoryName.isEmpty() && mapCategoryName.containsKey(p.getCategoryId())) {
                    p.setCategoryName(mapCategoryName.get(p.getCategoryId()));
                }

                if (!CollectionUtils.isEmpty(mapTopicStatusStudentRegistry)
                        && mapTopicStatusStudentRegistry.containsKey(p.getId())) {
                    p.setStudentRegistry(mapTopicStatusStudentRegistry.get(p.getId()));
                }
            });
        }
    }

    private Map<Long, LecturerDTO> setLecture(List<Long> lectureIds) {
        return getLongLecturerDTOMap(lectureIds, lecturerReps, lecturerMapper, userInfoReps);
    }

    private Map<Long, String> mapTopicWithCategoryName(List<Long> categoryIds) {
        Map<Long, String> mapIdCategoryWithCategoryName = new HashMap<>();
        if (categoryIds != null && !categoryIds.isEmpty()) {
            mapIdCategoryWithCategoryName = categoryReps.findByIdIn(categoryIds)
                    .stream().collect(Collectors.toMap(Categories::getId, Categories::getName));
        }

        return mapIdCategoryWithCategoryName;
    }

    private Map<Long, List<String>> mapTopicIdWithListFileId(List<Long> topicIds) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<Long, List<String>> map = new HashMap<>();

        List<Topics> topics = topicReps.findByIdIn(topicIds);
        if (!topics.isEmpty()) {
            topics.forEach(t -> {
                List<String> fileIds = new ArrayList<>();
                if (t.getFileId() != null && !t.getFileId().isEmpty()) {
                    try {
                        fileIds = objectMapper.readValue(t.getFileId(), List.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
                map.put(t.getId(), fileIds);
            });
        }

        return map;
    }

    @Override
    public PageDataResponse<TopicDTO> getTopicOfStudentApproved(SearchTopicStudentRequest request) {
        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser user = (CustomUser) authentication.getPrincipal();

        Optional<Students> students = studentReps.findByUserId(user.getId());

        if (students.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy sinh viên");
        }

        List<StudentTopic> studentTopics =
                studentTopicReps.findByStudentIdInAndStatusIsTrue(Collections.singletonList(students.get().getId()));
        Map<Long, Boolean> mapTopicStatusStudentRegistry = studentTopics.stream()
                .collect(Collectors.toMap(StudentTopic::getTopicId, StudentTopic::getStatus));
        List<Long> topicIds = studentTopics.stream().map(StudentTopic::getTopicId).distinct().collect(Collectors.toList());
        request.setTopicIds(topicIds);
        Page<TopicDTO> topicDTOS = topicReps.getListByTopicIds(request, pageable).map(topicMapper::to);
        setTopicDTO(topicDTOS, topicIds, mapTopicStatusStudentRegistry);

        return PageDataResponse.of(topicDTOS);
    }
}
