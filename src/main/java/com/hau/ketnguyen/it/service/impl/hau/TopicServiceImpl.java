package com.hau.ketnguyen.it.service.impl.hau;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.BeanUtil;
import com.hau.ketnguyen.it.common.util.PageableUtils;
import com.hau.ketnguyen.it.entity.hau.Lecturers;
import com.hau.ketnguyen.it.entity.hau.Topics;
import com.hau.ketnguyen.it.entity.hau.UserInfo;
import com.hau.ketnguyen.it.model.dto.hau.LecturerDTO;
import com.hau.ketnguyen.it.model.dto.hau.StatisticalDTO;
import com.hau.ketnguyen.it.model.dto.hau.TopicDTO;
import com.hau.ketnguyen.it.model.request.auth.SearchRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchTopicRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.repository.auth.UserInfoReps;
import com.hau.ketnguyen.it.repository.hau.LecturerReps;
import com.hau.ketnguyen.it.repository.hau.TopicReps;
import com.hau.ketnguyen.it.service.FileService;
import com.hau.ketnguyen.it.service.GoogleDriverFile;
import com.hau.ketnguyen.it.service.TopicService;
import com.hau.ketnguyen.it.service.mapper.LecturerMapper;
import com.hau.ketnguyen.it.service.mapper.TopicMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    private final FileService fileService;

    @Override
    public TopicDTO save(TopicDTO topicDTO) {
        return topicMapper.to(topicReps.save(topicMapper.from(topicDTO)));
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
        ObjectMapper objectMapper = new ObjectMapper();
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

        if (!fileIdsLong.isEmpty() && fileIdsLong.containsKey(id)) {
            topicDTO.setFileIds(fileIdsLong.get(id));
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

        if (!page.isEmpty()) {
            List<Long> topicIds = page.map(TopicDTO::getId).toList();
            List<Long> lectureGuideIds = page.map(TopicDTO::getLecturerGuideId).stream().distinct().collect(Collectors.toList());
            List<Long> lectureCounterArgumentIds = page.map(TopicDTO::getLecturerCounterArgumentId).stream().distinct().collect(Collectors.toList());

            List<Long> lectureIds = new ArrayList<>();
            lectureIds.addAll(lectureGuideIds);
            lectureIds.addAll(lectureCounterArgumentIds);

            Map<Long, LecturerDTO> lecturerDTOMap = setLecture(lectureIds.stream().distinct().collect(Collectors.toList()));
            Map<Long, List<String>> mapFileIds = mapTopicIdWithListFileId(topicIds);
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
            });
        }

        return PageDataResponse.of(page);
    }

    private Map<Long, LecturerDTO> setLecture(List<Long> lectureIds) {
        return getLongLecturerDTOMap(lectureIds, lecturerReps, lecturerMapper, userInfoReps);
    }

    @Override
    public void uploadFile(MultipartFile[] file, String filePath, boolean isPublic, Long topicId) {
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
    }

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
            float avg = (u.getScoreAssembly() + u.getScoreGuide() + u.getScoreCounterArgument()) / 3;
            statisticalDTO.setScoreMedium(avg);
            return statisticalDTO;
        }).collect(Collectors.toList());

        Long total = topicReps.getStatisticalTotal();

        return PageDataResponse.of(String.valueOf(total), page);
    }
}
