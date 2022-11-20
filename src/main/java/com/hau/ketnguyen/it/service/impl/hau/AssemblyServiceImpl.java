package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.PageableUtils;
import com.hau.ketnguyen.it.entity.hau.Assemblies;
import com.hau.ketnguyen.it.entity.hau.Lecturers;
import com.hau.ketnguyen.it.entity.hau.Topics;
import com.hau.ketnguyen.it.model.dto.hau.AssemblyDTO;
import com.hau.ketnguyen.it.model.dto.hau.CommentDTO;
import com.hau.ketnguyen.it.model.dto.hau.LecturerDTO;
import com.hau.ketnguyen.it.model.dto.hau.TopicDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchAssemblyRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    @Override
    public AssemblyDTO save(AssemblyDTO assemblyDTO) {
        return assemblyMapper.to(assemblyReps.save(assemblyMapper.from(assemblyDTO)));
    }

    @Override
    public AssemblyDTO edit(Long id, AssemblyDTO assemblyDTO) {
        Optional<Assemblies> assembliesOptional = assemblyReps.findById(id);

        if (assembliesOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy hội đồng");
        }

        Assemblies assemblies = assembliesOptional.get();
        assemblyMapper.copy(assemblyDTO, assemblies);

        return assemblyMapper.to(assemblies);
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
        Optional<Assemblies> assembliesOptional = assemblyReps.findById(id);

        if (assembliesOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy hội đồng");
        }

        Topics topics = topicReps.findById(assembliesOptional.get().getTopicId())
                .orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy đề tài"));
        TopicDTO topicDTO = topicMapper.to(topics);
        Lecturers lecturers = lecturerReps.findById(assembliesOptional.get().getLecturerId())
                .orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy giảng viên"));
        LecturerDTO lecturerDTO = lecturerMapper.to(lecturers);

        AssemblyDTO assemblyDTO = assemblyMapper.to(assembliesOptional.get());
        assemblyDTO.setTopicDTO(topicDTO);
        assemblyDTO.setLecturerDTO(lecturerDTO);

        return assemblyDTO;
    }

    @Override
    public PageDataResponse<AssemblyDTO> getAll(SearchAssemblyRequest request) {
        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        Page<AssemblyDTO> assemblyDTOS = assemblyReps.search(request, pageable).map(assemblyMapper::to);

        if (!assemblyDTOS.isEmpty()) {
            List<Long> topicIds = assemblyDTOS.map(AssemblyDTO::getTopicId).toList();
            List<Long> lectureIds = assemblyDTOS.map(AssemblyDTO::getLecturerId).toList();

            Map<Long, LecturerDTO> lecturerDTOMap = lecturerReps.findByIdIn(lectureIds)
                    .stream().map(lecturerMapper::to).collect(Collectors.toMap(LecturerDTO::getId, l -> l));
            Map<Long, TopicDTO> topicDTOMap = topicReps.findByIdIn(topicIds)
                    .stream().map(topicMapper::to).collect(Collectors.toMap(TopicDTO::getId, t -> t));

            assemblyDTOS.forEach(a -> {
                if (!lecturerDTOMap.isEmpty() && lecturerDTOMap.containsKey(a.getLecturerId())) {
                    a.setLecturerDTO(lecturerDTOMap.get(a.getLecturerId()));
                }

                if (!topicDTOMap.isEmpty() && topicDTOMap.containsKey(a.getTopicId())) {
                    a.setTopicDTO(topicDTOMap.get(a.getTopicId()));
                }
            });
        }

        return PageDataResponse.of(assemblyDTOS);
    }
}
