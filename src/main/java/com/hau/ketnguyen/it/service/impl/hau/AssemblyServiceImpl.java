package com.hau.ketnguyen.it.service.impl.hau;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.BeanUtil;
import com.hau.ketnguyen.it.common.util.PageableUtils;
import com.hau.ketnguyen.it.entity.hau.Assemblies;
import com.hau.ketnguyen.it.entity.hau.Lecturers;
import com.hau.ketnguyen.it.entity.hau.Topics;
import com.hau.ketnguyen.it.entity.hau.UserInfo;
import com.hau.ketnguyen.it.model.dto.hau.AssemblyDTO;
import com.hau.ketnguyen.it.model.dto.hau.LecturerDTO;
import com.hau.ketnguyen.it.model.dto.hau.TopicDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchAssemblyRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.repository.auth.UserInfoReps;
import com.hau.ketnguyen.it.repository.hau.AssemblyReps;
import com.hau.ketnguyen.it.repository.hau.LecturerReps;
import com.hau.ketnguyen.it.repository.hau.TopicReps;
import com.hau.ketnguyen.it.service.AssemblyService;
import com.hau.ketnguyen.it.service.mapper.AssemblyMapper;
import com.hau.ketnguyen.it.service.mapper.LecturerMapper;
import com.hau.ketnguyen.it.service.mapper.TopicMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AssemblyServiceImpl implements AssemblyService {
    private final AssemblyMapper assemblyMapper;
    private final AssemblyReps assemblyReps;
    private final TopicReps topicReps;
    private final TopicMapper topicMapper;
    private final LecturerReps lecturerReps;
    private final LecturerMapper lecturerMapper;
    private final UserInfoReps userInfoReps;

    @Override
    public AssemblyDTO save(AssemblyDTO assemblyDTO) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (assemblyDTO.getIdLectures() != null && !assemblyDTO.getIdLectures().isEmpty()) {
            String lectureIds = null;
            try {
                lectureIds = objectMapper.writeValueAsString(assemblyDTO.getIdLectures());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            assemblyDTO.setLecturerIds(lectureIds);
        }

        return assemblyMapper.to(assemblyReps.save(assemblyMapper.from(assemblyDTO)));
    }

    @Override
    public AssemblyDTO edit(Long id, AssemblyDTO assemblyDTO) {
        Optional<Assemblies> assembliesOptional = assemblyReps.findById(id);

        if (assembliesOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy hội đồng");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        if (assemblyDTO.getIdLectures() != null && !assemblyDTO.getIdLectures().isEmpty()) {
            String lectureIds = null;
            try {
                lectureIds = objectMapper.writeValueAsString(assemblyDTO.getIdLectures());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            assemblyDTO.setLecturerIds(lectureIds);
        }

        Assemblies assemblies = assembliesOptional.get();
        BeanUtil.copyNonNullProperties(assemblyDTO, assemblies);

        return assemblyMapper.to(assemblyReps.save(assemblies));
    }

    @Override
    public void delete(Long id) {
        Optional<Assemblies> assembliesOptional = assemblyReps.findById(id);

        if (assembliesOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy hội đồng");
        }

        assemblyReps.delete(assembliesOptional.get());
    }

    @Override
    public AssemblyDTO findById(Long id) {
        ObjectMapper objectMapper = new ObjectMapper();
        Optional<Assemblies> assembliesOptional = assemblyReps.findById(id);

        if (assembliesOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy hội đồng");
        }

        AssemblyDTO assemblyDTO = assemblyMapper.to(assembliesOptional.get());
        if (assembliesOptional.get().getTopicId() != null) {
            Topics topics = topicReps.findById(assembliesOptional.get().getTopicId())
                    .orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy đề tài"));
            TopicDTO topicDTO = topicMapper.to(topics);
            assemblyDTO.setTopicDTO(topicDTO);
        }

        if (assemblyDTO.getLecturerIds() != null && !assemblyDTO.getLecturerIds().isEmpty()) {
            List<Integer> lectureIds = null;
            try {
                if (assemblyDTO.getLecturerIds() != null) {
                    lectureIds = objectMapper.readValue(assemblyDTO.getLecturerIds(), List.class);
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            List<Long> lectureIdLong = new ArrayList<>();

            if (lectureIds != null && !lectureIds.isEmpty()) {
                lectureIds.forEach(l -> lectureIdLong.add(Long.parseLong(l.toString())));
            }

            assemblyDTO.setLecturerDTOS(new ArrayList<>(setLecture(lectureIdLong).values()));
        }

        return assemblyDTO;
    }

    @Override
    public PageDataResponse<AssemblyDTO> getAll(SearchAssemblyRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        if (request.getNameAssembly() != null) {
            request.setNameAssembly(request.getNameAssembly().toLowerCase());
        }

        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        Page<AssemblyDTO> assemblyDTOS = assemblyReps.search(request, pageable).map(assemblyMapper::to);

        if (!assemblyDTOS.isEmpty()) {
            List<Long> topicIds = assemblyDTOS.map(AssemblyDTO::getTopicId).toList();
            Map<Long, TopicDTO> topicDTOMap = topicReps.findByIdIn(topicIds)
                    .stream().map(topicMapper::to).collect(Collectors.toMap(TopicDTO::getId, t -> t));

            Map<Long, List<Long>> mapTopicLectrueIds = new HashMap<>();
            List<Long> idLectures = new ArrayList<>();
            assemblyDTOS.forEach(l -> {
                List<Integer> lectureIds = null;
                try {
                    if (l.getLecturerIds() != null) {
                        lectureIds = objectMapper.readValue(l.getLecturerIds(), List.class);
                    }
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                List<Long> lectureIdLong = new ArrayList<>();

                if (lectureIds != null && !lectureIds.isEmpty()) {
                    lectureIds.forEach(i -> lectureIdLong.add(Long.parseLong(i.toString())));
                }

                idLectures.addAll(lectureIdLong);
                mapTopicLectrueIds.put(l.getId(), lectureIdLong);
            });

            Map<Long, LecturerDTO> lecturerDTOMap = setLecture(idLectures);
            Map<Long, List<LecturerDTO>> mapTopicWithLectureDTO = new HashMap<>();
            if (!mapTopicLectrueIds.isEmpty()) {
                mapTopicLectrueIds.forEach((k, v) -> {
                    if (v != null && !v.isEmpty()) {
                        List<LecturerDTO> list = new ArrayList<>();
                        v.forEach(i -> {
                            if (!lecturerDTOMap.isEmpty() && lecturerDTOMap.containsKey(i)) {
                                list.add(lecturerDTOMap.get(i));
                            }
                        });
                        mapTopicWithLectureDTO.put(k, list);
                    }
                });
            }

            assemblyDTOS.forEach(a -> {
                if (!topicDTOMap.isEmpty() && topicDTOMap.containsKey(a.getTopicId())) {
                    a.setTopicDTO(topicDTOMap.get(a.getTopicId()));
                }

                if (!mapTopicWithLectureDTO.isEmpty() && mapTopicWithLectureDTO.containsKey(a.getTopicId())) {
                    a.setLecturerDTOS(mapTopicWithLectureDTO.get(a.getTopicId()));
                }
            });
        }

        return PageDataResponse.of(assemblyDTOS);
    }

    private Map<Long, LecturerDTO> setLecture(List<Long> lectureIds) {
        return getLongLecturerDTOMap(lectureIds, lecturerReps, lecturerMapper, userInfoReps);
    }

    static Map<Long, LecturerDTO> getLongLecturerDTOMap(List<Long> lectureIds, LecturerReps lecturerReps, LecturerMapper lecturerMapper, UserInfoReps userInfoReps) {
        Map<Long, LecturerDTO> lecturerDTOMap = new HashMap<>();
        if (lectureIds != null && !lectureIds.isEmpty()) {
            List<Lecturers> lecturers = lecturerReps.findByIdIn(lectureIds);
            lecturerDTOMap = lecturers.stream().map(lecturerMapper::to)
                    .collect(Collectors.toMap(LecturerDTO::getId, l -> l));

            if (!lecturers.isEmpty()) {
                List<Long> userIdInfos = lecturers.stream().map(Lecturers::getUserInfoId).distinct().collect(Collectors.toList());
                Map<Long, String> userInfos = userInfoReps.findByIdIn(userIdInfos).stream()
                        .collect(Collectors.toMap(UserInfo::getId, UserInfo::getFullName));
                lecturerDTOMap.forEach((k, v) -> {
                    if (v != null) {
                        if (!userInfos.isEmpty() && userInfos.containsKey(v.getUserInfoId())) {
                            v.setFullName(userInfos.get(v.getUserInfoId()));
                        }
                    }
                });
            }
        }
        return lecturerDTOMap;
    }
}
