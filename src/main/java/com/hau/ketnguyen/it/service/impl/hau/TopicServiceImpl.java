package com.hau.ketnguyen.it.service.impl.hau;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.BeanUtil;
import com.hau.ketnguyen.it.common.util.PageableUtils;
import com.hau.ketnguyen.it.entity.auth.CustomUser;
import com.hau.ketnguyen.it.entity.hau.*;
import com.hau.ketnguyen.it.model.dto.hau.LecturerDTO;
import com.hau.ketnguyen.it.model.dto.hau.StatisticalDTO;
import com.hau.ketnguyen.it.model.dto.hau.StudentSuggestTopicDTO;
import com.hau.ketnguyen.it.model.dto.hau.TopicDTO;
import com.hau.ketnguyen.it.model.request.auth.SearchRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchStudentTopicRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchTopicRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.repository.auth.UserInfoReps;
import com.hau.ketnguyen.it.repository.hau.*;
import com.hau.ketnguyen.it.service.GoogleDriverFile;
import com.hau.ketnguyen.it.service.TopicService;
import com.hau.ketnguyen.it.service.mapper.LecturerMapper;
import com.hau.ketnguyen.it.service.mapper.StudentSuggestTopicMapper;
import com.hau.ketnguyen.it.service.mapper.StudentTopicMapper;
import com.hau.ketnguyen.it.service.mapper.TopicMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static com.hau.ketnguyen.it.service.impl.hau.AssemblyServiceImpl.getLongLecturerDTOMap;

@Slf4j
@Service
@AllArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicReps topicReps;
    private final TopicMapper topicMapper;
    private final LecturerReps lecturerReps;
    private final LecturerMapper lecturerMapper;
    private final UserInfoReps userInfoReps;
    private final GoogleDriverFile googleDriverFile;
    private final CategoryReps categoryReps;
    private final StudentTopicReps studentTopicReps;
    private final StudentReps studentReps;
    private final StudentTopicMapper studentTopicMapper;
    private final StudentSuggestTopicReps studentSuggestTopicReps;
    private final StudentSuggestTopicMapper studentSuggestTopicMapper;

    @Override
    public TopicDTO save(TopicDTO topicDTO) {
        validateNameTopic(topicDTO.getName());
        topicDTO.setStatusSuggest(true);
        return topicMapper.to(topicReps.save(topicMapper.from(topicDTO)));
    }

    private void validateNameTopic(String name) {
        Optional<Topics> topicsOptional = topicReps.findByName(name.toLowerCase());
        if (topicsOptional.isPresent()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Tên đề tài đã tồn tại");
        }
    }

    @Override
    public TopicDTO edit(Long id, TopicDTO topicDTO) {
        Optional<Topics> topicsOptional = topicReps.findById(id);

        if (topicsOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy đề tài");
        }

        Topics topics = topicsOptional.get();
        BeanUtil.copyNonNullProperties(topicDTO, topics);

        return topicMapper.to(topicReps.save(topics));
    }

    @Override
    public void delete(Long id) {
        Optional<Topics> topicsOptional = topicReps.findById(id);

        if (topicsOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy đề tài");
        }

        topicReps.delete(topicsOptional.get());
    }

    @Override
    public TopicDTO findById(Long id) {
        Optional<Topics> topicsOptional = topicReps.findById(id);

        if (topicsOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy đề tài");
        }

        TopicDTO topicDTO = topicMapper.to(topicsOptional.get());
        if (topicDTO.getLecturerGuideId() != null || topicDTO.getLecturerCounterArgumentId() != null) {
            List<Long> lectureIds = new ArrayList<>();

            if (topicDTO.getLecturerGuideId() != null) {
                lectureIds.add(topicDTO.getLecturerGuideId());
            }

            if (topicDTO.getLecturerCounterArgumentId() != null) {
                lectureIds.add(topicDTO.getLecturerCounterArgumentId());
            }

            Map<Long, LecturerDTO> lecturerDTOMap = setLecture(lectureIds.stream().distinct().collect(Collectors.toList()));

            if (lecturerDTOMap.isEmpty()) {
                throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy giảng viên");
            }

            if (lecturerDTOMap.containsKey(topicDTO.getLecturerGuideId())) {
                topicDTO.setLecturerGuideDTO(lecturerDTOMap.get(topicDTO.getLecturerGuideId()));
            }

            if (lecturerDTOMap.containsKey(topicDTO.getLecturerCounterArgumentId())) {
                topicDTO.setLecturerCounterArgumentDTO(lecturerDTOMap.get(topicDTO.getLecturerCounterArgumentId()));
            }
        }

        Map<Long, List<String>> fileIdsLong = mapTopicIdWithListFileId(Collections.singletonList(id));
        Map<Long, Boolean> getStatusRegistryTopicByCurrentUser = getStatusRegistryTopicByCurrentUser();
        Map<Long, Boolean> getStatusApproveTopicByCurrentUser = getStatusApproveTopicByCurrentUser();
        Map<Long, String> mapCategoryName = mapTopicWithCategoryName(Collections.singletonList(topicDTO.getCategoryId()));

        if (!fileIdsLong.isEmpty() && fileIdsLong.containsKey(id)) {
            topicDTO.setFileIds(fileIdsLong.get(id));
        }

        if (!CollectionUtils.isEmpty(getStatusRegistryTopicByCurrentUser)
                && getStatusRegistryTopicByCurrentUser.containsKey(topicDTO.getId())) {
            topicDTO.setStudentRegistry(getStatusRegistryTopicByCurrentUser.get(topicDTO.getId()));
        }

        if (!CollectionUtils.isEmpty(getStatusApproveTopicByCurrentUser)
                && getStatusApproveTopicByCurrentUser.containsKey(topicDTO.getId())) {
            topicDTO.setStudentApprove(getStatusApproveTopicByCurrentUser.get(topicDTO.getId()));
        }

        if (!CollectionUtils.isEmpty(mapCategoryName) && mapCategoryName.containsKey(topicDTO.getCategoryId())) {
            topicDTO.setCategoryName(mapCategoryName.get(topicDTO.getCategoryId()));
        }

        return topicDTO;
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
    public PageDataResponse<TopicDTO> getAll(SearchTopicRequest request) {
        if (request.getName() != null) {
            request.setName(request.getName().toLowerCase());
        }

        if (request.getDescription() != null) {
            request.setDescription(request.getDescription().toLowerCase());
        }

        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        Page<TopicDTO> page = topicReps.search(request, pageable).map(topicMapper::to);
        return getTopicDTOPageDataResponse(page);
    }

    /* Trạng thái đăng ký đề tài của người dùng hiện tại*/
    private Map<Long, Boolean> getStatusRegistryTopicByCurrentUser() {
        Map<Long, Boolean> map = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser customUser = (CustomUser) authentication.getPrincipal();

        Optional<Students> students = studentReps.findByUserId(customUser.getId());

        if (students.isPresent()) {
            List<StudentTopic> studentTopics = studentTopicReps.findByStudentId(students.get().getId());
            if (!CollectionUtils.isEmpty(studentTopics)) {
                studentTopics.forEach(s -> map.put(s.getTopicId(), s.getStatusRegistry()));
            }
        }

        return map;
    }

    /* Trạng thái duyệt đề tài của người dùng hiện tại*/
    private Map<Long, Boolean> getStatusApproveTopicByCurrentUser() {
        Map<Long, Boolean> map = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser customUser = (CustomUser) authentication.getPrincipal();

        Optional<Students> students = studentReps.findByUserId(customUser.getId());

        if (students.isPresent()) {
            List<StudentTopic> studentTopics = studentTopicReps.findByStudentId(students.get().getId());
            if (!CollectionUtils.isEmpty(studentTopics)) {
                studentTopics.forEach(s -> map.put(s.getTopicId(), s.getStatusApprove()));
            }
        }

        return map;
    }

    private Map<Long, LecturerDTO> setLecture(List<Long> lectureIds) {
        return getLongLecturerDTOMap(lectureIds, lecturerReps, lecturerMapper, userInfoReps);
    }

    @Override
    public List<String> uploadFile(MultipartFile[] file, String filePath, boolean isPublic, Long topicId) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> fileIds = googleDriverFile.uploadMultiFile(file, filePath, isPublic);
        if (fileIds != null && !fileIds.isEmpty()) {
            Optional<Topics> topicsOptional = topicReps.findById(topicId);

            if (topicsOptional.isEmpty()) {
                throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy đề tài");
            }

            Topics topic = topicsOptional.get();
            String fileId = null;
            try {
                fileId = objectMapper.writeValueAsString(fileIds);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            topic.setFileId(fileId);
            topicReps.save(topic);
        }
        return fileIds;
    }

    private Map<Long, String> mapTopicWithCategoryName(List<Long> categoryIds) {
        Map<Long, String> mapIdCategoryWithCategoryName = new HashMap<>();
        if (categoryIds != null && !categoryIds.isEmpty()) {
            mapIdCategoryWithCategoryName = categoryReps.findByIdIn(categoryIds)
                    .stream().collect(Collectors.toMap(Categories::getId, Categories::getName));
        }

        return mapIdCategoryWithCategoryName;
    }

    /* Thống kê điểm */
    @Override
    public PageDataResponse<StatisticalDTO> getStatistical(SearchRequest request) {
        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        List<StatisticalDTO> page = topicReps.getStatistical(pageable).stream().map(u -> {
            StatisticalDTO statisticalDTO = new StatisticalDTO();
            statisticalDTO.setNameTopic(u.getNameTopic());
            statisticalDTO.setNameClass(u.getNameClass());
            statisticalDTO.setNameStudent(u.getNameStudent());
            statisticalDTO.setTopicYear(u.getTopicYear());
            statisticalDTO.setScoreAssembly(u.getScoreAssembly());
            statisticalDTO.setScoreGuide(u.getScoreGuide());
            statisticalDTO.setScoreCounterArgument(u.getScoreCounterArgument());
            statisticalDTO.setScoreProcessOne(u.getScoreProcessOne());
            statisticalDTO.setScoreProcessTwo(u.getScoreProcessTwo() );
            float avg = (u.getScoreAssembly() + u.getScoreGuide() + u.getScoreCounterArgument()) / 3;
            statisticalDTO.setScoreMedium(avg);
            return statisticalDTO;
        }).collect(Collectors.toList());

        Long total = topicReps.getStatisticalTotal();

        return PageDataResponse.of(String.valueOf(total), page);
    }

    /* Danh sách đề tài sinh viên đề xuất -> màn người quản trị -> để duyệt*/
    @Override
    public PageDataResponse<TopicDTO> getListTopicSuggest(SearchTopicRequest request) {
        if (request.getName() != null) {
            request.setName(request.getName().toLowerCase());
        }

        if (request.getDescription() != null) {
            request.setDescription(request.getDescription().toLowerCase());
        }

        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        Page<TopicDTO> page = topicReps.searchTopicSuggest(request, pageable).map(topicMapper::to);
        return getTopicDTOPageDataResponse(page);
    }

    @Override
    public PageDataResponse<StudentSuggestTopicDTO> getListStudentSuggestTopic(SearchStudentTopicRequest request) {
        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());

        Page<StudentSuggestTopicDTO> studentTopics = studentSuggestTopicReps.search(request, pageable).map(studentSuggestTopicMapper::to);
        if (!CollectionUtils.isEmpty(studentTopics.toList())) {
            List<Long> topicIds = studentTopics.toList().stream()
                    .map(StudentSuggestTopicDTO::getTopicId).distinct().collect(Collectors.toList());
            List<Topics> topics = topicReps.findByIdIn(topicIds);
            Map<Long, String> mapTopicName = new HashMap<>();
            if (!CollectionUtils.isEmpty(topics)) {
                mapTopicName = topics.stream().collect(Collectors.toMap(Topics::getId, Topics::getName));
            }

            List<Long> studentIds = studentTopics.stream().map(StudentSuggestTopicDTO::getStudentId).collect(Collectors.toList());
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

            for (StudentSuggestTopicDTO s : studentTopics) {
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

    public PageDataResponse<TopicDTO> getTopicDTOPageDataResponse(Page<TopicDTO> page) {
        Map<Long, Boolean> getStatusRegistryTopicByCurrentUser = getStatusRegistryTopicByCurrentUser();
        Map<Long, Boolean> getStatusApproveTopicByCurrentUser = getStatusApproveTopicByCurrentUser();

        if (!page.isEmpty()) {
            List<Long> topicIds = page.map(TopicDTO::getId).toList();
            List<Long> categoryIds = page.map(TopicDTO::getCategoryId).toList();
            List<Long> lectureGuideIds = page.map(TopicDTO::getLecturerGuideId).stream().distinct().collect(Collectors.toList());
            List<Long> lectureCounterArgumentIds = page.map(TopicDTO::getLecturerCounterArgumentId).stream().distinct().collect(Collectors.toList());

            List<Long> lectureIds = new ArrayList<>();
            lectureIds.addAll(lectureGuideIds);
            lectureIds.addAll(lectureCounterArgumentIds);

            Map<Long, LecturerDTO> lecturerDTOMap = setLecture(lectureIds.stream().distinct().collect(Collectors.toList()));
            Map<Long, List<String>> mapFileIds = mapTopicIdWithListFileId(topicIds);
            Map<Long, String> mapCategoryName = mapTopicWithCategoryName(categoryIds);
            page.forEach(p -> {
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

                if (!CollectionUtils.isEmpty(getStatusRegistryTopicByCurrentUser)
                        && getStatusRegistryTopicByCurrentUser.containsKey(p.getId())) {
                    p.setStudentRegistry(getStatusRegistryTopicByCurrentUser.get(p.getId()));
                }

                if (!CollectionUtils.isEmpty(getStatusApproveTopicByCurrentUser)
                        && getStatusApproveTopicByCurrentUser.containsKey(p.getId())) {
                    p.setStudentApprove(getStatusApproveTopicByCurrentUser.get(p.getId()));
                }
            });
        }

        return PageDataResponse.of(page);
    }
}
