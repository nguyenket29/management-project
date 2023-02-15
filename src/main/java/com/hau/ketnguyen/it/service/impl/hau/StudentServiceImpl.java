package com.hau.ketnguyen.it.service.impl.hau;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.BeanUtil;
import com.hau.ketnguyen.it.common.util.PageableUtils;
import com.hau.ketnguyen.it.common.util.StringUtils;
import com.hau.ketnguyen.it.entity.auth.CustomUser;
import com.hau.ketnguyen.it.entity.hau.*;
import com.hau.ketnguyen.it.model.dto.auth.UserInfoDTO;
import com.hau.ketnguyen.it.model.dto.hau.*;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.hau.ketnguyen.it.common.constant.Constants.APPROVED;
import static com.hau.ketnguyen.it.common.constant.Constants.WAITING_APPROVE;
import static com.hau.ketnguyen.it.common.enums.TypeUser.OTHER;
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
    private final StudentSuggestTopicReps studentSuggestTopicReps;
    private final FileReps fileReps;
    @Override
    @Transactional
    public StudentDTO save(StudentDTO studentDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser user = (CustomUser) authentication.getPrincipal();

        if (StringUtils.isNullOrEmpty(user.getType()) || user.getType().equalsIgnoreCase(OTHER.name())) {
            UserInfo userInfo = setUserInfo(studentDTO);
            userInfoReps.save(userInfo);
            studentDTO.setUserInfoId(userInfo.getId());
            if (!StringUtils.isNullOrEmpty(studentDTO.getTopicId().toString())) {
                validateRegistryTopicOnlyStudent(studentDTO.getTopicId());
            }
            Students students = studentReps.save(studentMapper.from(studentDTO));

            // Nếu tạo mới sinh viên đó có chọn đề tài thì đề tài đó sẽ được duyệt cho sinh viên đó
            if (studentDTO.getTopicId() != null) {
                StudentTopic studentTopic = new StudentTopic();
                studentTopic.setStatusApprove(true);
                studentTopic.setStatusRegistry(true);
                studentTopic.setTopicId(students.getTopicId());
                studentTopic.setStudentId(students.getId());

                studentTopicReps.save(studentTopic);
            }

            return studentMapper.to(students);
        } else {
            throw APIException.from(HttpStatus.BAD_REQUEST).withMessage("Người dùng hiện tại không có quyền.");
        }
    }

    private void validateRegistryTopicOnlyStudent(Long topic) {
        Optional<Students> studentsOptional = studentReps.findByTopicId(topic);
        if (studentsOptional.isPresent()) {
            throw APIException.from(HttpStatus.BAD_REQUEST).withMessage("Đề tài đã có sinh viên đăng ký, vui lòng chọn đề tài khác.");
        }
    }

    @Override
    @Transactional
    public StudentDTO edit(Long id, StudentDTO studentDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser user = (CustomUser) authentication.getPrincipal();

        if (StringUtils.isNullOrEmpty(user.getType()) || user.getType().equalsIgnoreCase(OTHER.name())) {
            Optional<Students> studentOptional = studentReps.findById(id);

            if (studentOptional.isEmpty()) {
                throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy sinh viên");
            }

            Long topicIdOld = studentOptional.get().getTopicId();

            if (studentDTO.getTopicId() != null && !StringUtils.isNullOrEmpty(studentDTO.getTopicId().toString())) {
                validateRegistryTopicOnlyStudent(studentDTO.getTopicId());
            }

            Students students = studentOptional.get();
            Long userInfoId = students.getUserInfoId();
            BeanUtil.copyNonNullProperties(studentDTO, students);
            if (studentDTO.getTopicId() == null) {
                students.setTopicId(null);
            }
            studentReps.save(students);

            Optional<UserInfo> userInfoOptional = userInfoReps.findById(userInfoId);

            if (userInfoOptional.isEmpty()) {
                throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy thông tin sinh viên");
            }

            UserInfo userInfo = setUserInfo(studentDTO);
            BeanUtil.copyNonNullProperties(userInfo, userInfoOptional.get());
            userInfoReps.save(userInfoOptional.get());

            // Xóa đề tài cũ
            Optional<StudentTopic> studentTopicOptional =
                    studentTopicReps.findByStudentIdAndTopicId(students.getId(), topicIdOld);
            studentTopicOptional.ifPresent(studentTopicReps::delete);

            if ((studentDTO.getTopicId() != null && !Objects.equals(topicIdOld, studentDTO.getTopicId())) ||
                    (studentDTO.getTopicId() != null && topicIdOld == null)) {
                // Nếu tạo mới sinh viên đó có chọn đề tài thì đề tài đó sẽ được duyệt cho sinh viên đó
                StudentTopic studentTopic = new StudentTopic();
                studentTopic.setStatusApprove(true);
                studentTopic.setStatusRegistry(true);
                studentTopic.setTopicId(students.getTopicId());
                studentTopic.setStudentId(students.getId());

                studentTopicReps.save(studentTopic);
            }


            return studentMapper.to(students);
        } else {
            throw APIException.from(HttpStatus.BAD_REQUEST).withMessage("Người dùng hiện tại không có quyền.");
        }
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
    @Transactional
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

    /* Sinh viên đăng ký đề tài */
    @Override
    @Transactional
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
            studentTopic.setStatusRegistry(registry);
            studentTopicReps.save(studentTopic);
        }
    }

    /* Lấy danh sách đề tài sinh viên đăng ký*/
    @Override
    public PageDataResponse<TopicDTO> getListTopicRegistry(SearchTopicStudentRequest request) {
        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser user = (CustomUser) authentication.getPrincipal();

        Optional<Students> students = studentReps.findByUserId(user.getId());

        if (students.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy sinh viên");
        }

        List<StudentTopic> studentTopics = studentTopicReps
                .findByStudentIdInAndStatusRegistryIsTrue(Collections.singletonList(students.get().getId()));
        List<Long> topicIdApproved = studentTopicReps
                .findByStudentIdInAndStatusRegistryIsTrueAndStatusApproveIsTrue(Collections.singletonList(students.get().getId()))
                .stream().map(StudentTopic::getTopicId).collect(Collectors.toList());

        Map<Long, Boolean> mapTopicStatusStudentRegistry = studentTopics.stream()
                .collect(Collectors.toMap(StudentTopic::getTopicId, StudentTopic::getStatusRegistry));
        Map<Long, Boolean> mapTopicStatusApprove = studentTopics.stream()
                .collect(Collectors.toMap(StudentTopic::getTopicId, StudentTopic::getStatusApprove));
        List<Long> topicIds = studentTopics.stream().map(StudentTopic::getTopicId).distinct().collect(Collectors.toList());
        request.setTopicIds(topicIds);
        if (request.getTopicName() != null) {
            request.setTopicName(request.getTopicName().toLowerCase());
        }
        Page<TopicDTO> page = topicReps.getListByTopicIds(request, pageable).map(topicMapper::to);
        setTopicDTO(page, topicIds, mapTopicStatusStudentRegistry, mapTopicStatusApprove, topicIdApproved);

        return PageDataResponse.of(page);
    }

    private void setTopicDTO(Page<TopicDTO> topicDTOS, List<Long> topicIds,
                             Map<Long, Boolean> mapTopicStatusStudentRegistry,
                             Map<Long, Boolean> mapTopicStatusApprove, List<Long> topicIdApproved) {
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
            Map<Long, String> mapTopicWithFileNames = mapTopicWithFileNames(topicDTOS.toList());
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

                    if (!CollectionUtils.isEmpty(p.getFileIds())) {
                        List<FileDTO> fileDTOS = new ArrayList<>();
                        List<Long> fileIdLongs = p.getFileIds().stream().map(Long::parseLong).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(fileIdLongs)) {
                            fileIdLongs.forEach(f -> {
                                if (!CollectionUtils.isEmpty(mapTopicWithFileNames)
                                        && mapTopicWithFileNames.containsKey(f)) {
                                    FileDTO fileDTO = new FileDTO();
                                    fileDTO.setId(f);
                                    fileDTO.setName(mapTopicWithFileNames.get(f));
                                    fileDTOS.add(fileDTO);
                                }
                            });

                            p.setFileDTOS(fileDTOS);
                        }
                    }
                }

                if (!mapCategoryName.isEmpty() && mapCategoryName.containsKey(p.getCategoryId())) {
                    p.setCategoryName(mapCategoryName.get(p.getCategoryId()));
                }

                if (!CollectionUtils.isEmpty(mapTopicStatusStudentRegistry)
                        && mapTopicStatusStudentRegistry.containsKey(p.getId())) {
                    p.setStudentRegistry(mapTopicStatusStudentRegistry.get(p.getId()));
                }

                if (!CollectionUtils.isEmpty(mapTopicStatusApprove)
                        && mapTopicStatusApprove.containsKey(p.getId())) {
                    p.setStudentApprove(mapTopicStatusApprove.get(p.getId()));
                }

                if (!CollectionUtils.isEmpty(topicIdApproved) && topicIdApproved.contains(p.getId())) {
                    p.setStatusTopic(APPROVED);
                } else {
                    p.setStatusTopic(WAITING_APPROVE);
                }
            });
        }
    }

    private Map<Long, String> mapTopicWithFileNames(List<TopicDTO> topicDTOS) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<Long, String> filesMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(topicDTOS)) {
            List<String> fileIds = new ArrayList<>();
            for (TopicDTO t : topicDTOS) {
                if (t.getFileId() != null && !t.getFileId().isEmpty()) {
                    try {
                        fileIds.addAll(objectMapper.readValue(t.getFileId(), List.class));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            List<Long> idFiles = new ArrayList<>();
            if (!CollectionUtils.isEmpty(fileIds)) {
                idFiles.addAll(fileIds.stream().map(Long::parseLong).collect(Collectors.toList()));
            }

            filesMap = fileReps.findByIdIn(idFiles).stream().collect(Collectors.toMap(Files::getId, Files::getName));
        }
        return filesMap;
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

    /* Lấy danh sách đề tài mà sinh viên đăng ký đã được duyệt -> đề tài của tôi*/
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
                studentTopicReps.findByStudentIdInAndStatusApproveIsTrue(Collections.singletonList(students.get().getId()));
        List<Long> topicIdApproved = studentTopicReps
                .findByStudentIdInAndStatusRegistryIsTrueAndStatusApproveIsTrue(Collections.singletonList(students.get().getId()))
                .stream().map(StudentTopic::getTopicId).collect(Collectors.toList());
        Map<Long, Boolean> mapTopicStatusStudentRegistry = studentTopics.stream()
                .collect(Collectors.toMap(StudentTopic::getTopicId, StudentTopic::getStatusRegistry));
        Map<Long, Boolean> mapTopicStatusApprove = studentTopics.stream()
                .collect(Collectors.toMap(StudentTopic::getTopicId, StudentTopic::getStatusApprove));
        List<Long> topicIds = studentTopics.stream().map(StudentTopic::getTopicId).distinct().collect(Collectors.toList());
        request.setTopicIds(topicIds);
        if (request.getTopicName() != null) {
            request.setTopicName(request.getTopicName().toLowerCase());
        }
        Page<TopicDTO> topicDTOS = topicReps.getListByTopicIds(request, pageable).map(topicMapper::to);
        setTopicDTO(topicDTOS, topicIds, mapTopicStatusStudentRegistry, mapTopicStatusApprove, topicIdApproved);

        return PageDataResponse.of(topicDTOS);
    }

    /* Sinh viên đề xuất đề tài */
    @Override
    @Transactional
    public void studentSuggestTopic(String topicName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser user = (CustomUser) authentication.getPrincipal();

        Optional<Students> students = studentReps.findByUserId(user.getId());

        if (students.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy sinh viên");
        }

        // tạo mới đề tài
        TopicDTO topicDTO = TopicDTO.builder()
                .name(topicName)
                .statusSuggest(false)
                .build();
        Topics topics = topicReps.save(topicMapper.from(topicDTO));

        // đề xuất này của sinh viên nào
        StudentSuggestTopic studentSuggestTopic = new StudentSuggestTopic();
        studentSuggestTopic.setStatusSuggest(true);
        studentSuggestTopic.setTopicId(topics.getId());
        studentSuggestTopic.setStudentId(students.get().getId());
        studentSuggestTopicReps.save(studentSuggestTopic);
    }

    /* Người quản trị duyệt đề tài sinh viên đề xuất */
    @Override
    @Transactional
    public void adminApproveTopicSuggest(Long topicId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser user = (CustomUser) authentication.getPrincipal();

        if (StringUtils.isNullOrEmpty(user.getType()) || user.getType().equalsIgnoreCase(OTHER.name())) {
            Optional<Topics> topicsOptional = topicReps.findById(topicId);

            if (topicsOptional.isEmpty()) {
                throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy đề tài");
            }

            topicsOptional.get().setStatusSuggest(true);
            topicsOptional.get().setStatus(true);

            Optional<StudentSuggestTopic> studentSuggestTopic = studentSuggestTopicReps.findByTopicId(topicId);

            if (studentSuggestTopic.isEmpty()) {
                throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy đề tài");
            }

            studentSuggestTopic.get().setStatusApprove(true);
            studentSuggestTopicReps.save(studentSuggestTopic.get());
            topicReps.save(topicsOptional.get());
        } else {
            throw APIException.from(HttpStatus.BAD_REQUEST).withMessage("Người dùng không phải quản trị viên");
        }
    }

    /* Danh sách đề tài mà sinh viên đó đề xuất -> màn sinh viên */
    @Override
    public PageDataResponse<TopicDTO> getListTopicSuggestOfStudent(SearchTopicStudentRequest request) {
        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser user = (CustomUser) authentication.getPrincipal();

        Optional<Students> students = studentReps.findByUserId(user.getId());

        if (students.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy sinh viên");
        }

        List<StudentSuggestTopic> studentTopics =
                studentSuggestTopicReps.findByStudentIdInAndStatusSuggestIsTrue(Collections.singletonList(students.get().getId()));

        Map<Long, Boolean> mapTopicStatusApprove = studentTopics.stream()
                .collect(Collectors.toMap(StudentSuggestTopic::getTopicId, StudentSuggestTopic::getStatusApprove));
        List<Long> topicIds = studentTopics.stream().map(StudentSuggestTopic::getTopicId).distinct().collect(Collectors.toList());
        request.setTopicIds(topicIds);
        if (request.getTopicName() != null) {
            request.setTopicName(request.getTopicName().toLowerCase());
        }
        Page<TopicDTO> topicDTOS = topicReps.getListByTopicIds(request, pageable).map(topicMapper::to);
        setTopicDTO(topicDTOS, topicIds, null, mapTopicStatusApprove, null);

        return PageDataResponse.of(topicDTOS);
    }
}
