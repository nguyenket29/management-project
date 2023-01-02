package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.BeanUtil;
import com.hau.ketnguyen.it.common.util.PageableUtils;
import com.hau.ketnguyen.it.entity.hau.Topics;
import com.hau.ketnguyen.it.model.dto.hau.LecturerDTO;
import com.hau.ketnguyen.it.model.dto.hau.TopicDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchTopicRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.repository.hau.LecturerReps;
import com.hau.ketnguyen.it.repository.hau.TopicReps;
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

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicReps topicReps;
    private final TopicMapper topicMapper;
    private final LecturerReps lecturerReps;
    private final LecturerMapper lecturerMapper;
    private final GoogleDriverFile googleDriverFile;

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

            Map<Long, LecturerDTO> lecturerDTO = lecturerReps.findByIdIn(lectureIds).stream()
                    .map(lecturerMapper::to).collect(Collectors.toMap(LecturerDTO::getId, l -> l));

            if (lecturerDTO.isEmpty()) {
                throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy giảng viên");
            }

            if (lecturerDTO.containsKey(topicDTO.getLecturerGuideId())) {
                topicDTO.setLecturerGuideDTO(lecturerDTO.get(topicDTO.getLecturerGuideId()));
            }

            if (lecturerDTO.containsKey(topicDTO.getLecturerCounterArgumentId())) {
                topicDTO.setLecturerCounterArgumentDTO(lecturerDTO.get(topicDTO.getLecturerCounterArgumentId()));
            }
        }

        return topicDTO;
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
            List<Long> lectureGuideIds = page.map(TopicDTO::getLecturerGuideId).stream().distinct().collect(Collectors.toList());
            List<Long> lectureCounterArgumentIds = page.map(TopicDTO::getLecturerCounterArgumentId).stream().distinct().collect(Collectors.toList());

            List<Long> lectureIds = new ArrayList<>();
            lectureIds.addAll(lectureGuideIds);
            lectureIds.addAll(lectureCounterArgumentIds);

            Map<Long, LecturerDTO> lecturerDTOMap = setLecture(lectureIds.stream().distinct().collect(Collectors.toList()));
            page.forEach(p -> {
                if (p.getLecturerGuideId() != null && lecturerDTOMap.containsKey(p.getLecturerGuideId())) {
                    p.setLecturerGuideDTO(lecturerDTOMap.get(p.getLecturerGuideId()));
                }

                if (p.getLecturerCounterArgumentId() != null && lecturerDTOMap.containsKey(p.getLecturerCounterArgumentId())) {
                    p.setLecturerCounterArgumentDTO(lecturerDTOMap.get(p.getLecturerCounterArgumentId()));
                }
            });
        }

        return PageDataResponse.of(page);
    }

    private Map<Long, LecturerDTO> setLecture(List<Long> lectureIds) {
        Map<Long, LecturerDTO> lecturerDTOMap = new HashMap<>();
        if (lectureIds != null && !lectureIds.isEmpty()) {
            lecturerDTOMap = lecturerReps.findByIdIn(lectureIds).stream().map(lecturerMapper::to)
                    .collect(Collectors.toMap(LecturerDTO::getId, l -> l));
        }
        return lecturerDTOMap;
    }

    @Override
    public void uploadFile(MultipartFile file, String filePath, boolean isPublic, Long topicId) {
        String fileId = googleDriverFile.uploadFile(file, filePath, isPublic);
        if (fileId != null) {
            Optional<Topics> topicsOptional = topicReps.findById(topicId);

            if (topicsOptional.isEmpty()) {
                throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy đề tài");
            }

            Topics topic = topicsOptional.get();
            topic.setFileId(fileId);

            topicReps.save(topic);
        }
    }
}
