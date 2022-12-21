package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.BeanUtil;
import com.hau.ketnguyen.it.common.util.PageableUtils;
import com.hau.ketnguyen.it.entity.auth.CustomUser;
import com.hau.ketnguyen.it.entity.hau.Topics;
import com.hau.ketnguyen.it.entity.hau.UserInfo;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

        return topicMapper.to(topics);
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
        if (topicDTO.getLecturerId() != null) {
            LecturerDTO lecturerDTO = lecturerReps.findById(topicDTO.getLecturerId()).map(lecturerMapper::to)
                    .orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy giảng viên"));
            topicDTO.setLecturerDTO(lecturerDTO);
        }

        return topicDTO;
    }

    @Override
    public PageDataResponse<TopicDTO> getAll(SearchTopicRequest request) {
        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        Page<TopicDTO> page = topicReps.search(request, pageable).map(topicMapper::to);

        if (!page.isEmpty()) {
            List<Long> lectureIds = page.map(TopicDTO::getLecturerId).toList();
            Map<Long, LecturerDTO> lecturerDTOMap = setLecture(lectureIds);
            page.forEach(p -> {
                if (lecturerDTOMap.containsKey(p.getLecturerId())) {
                    p.setLecturerDTO(lecturerDTOMap.get(p.getLecturerId()));
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
