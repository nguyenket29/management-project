package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.BeanUtil;
import com.hau.ketnguyen.it.common.util.PageableUtils;
import com.hau.ketnguyen.it.entity.hau.Classes;
import com.hau.ketnguyen.it.entity.hau.Faculties;
import com.hau.ketnguyen.it.entity.hau.Workplaces;
import com.hau.ketnguyen.it.model.dto.hau.ClassDTO;
import com.hau.ketnguyen.it.model.dto.hau.FacultyDTO;
import com.hau.ketnguyen.it.model.dto.hau.StudentDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchClassRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.repository.auth.UserReps;
import com.hau.ketnguyen.it.repository.hau.ClassReps;
import com.hau.ketnguyen.it.repository.hau.FacultyReps;
import com.hau.ketnguyen.it.repository.hau.StudentReps;
import com.hau.ketnguyen.it.service.ClassService;
import com.hau.ketnguyen.it.service.mapper.ClassMapper;
import com.hau.ketnguyen.it.service.mapper.FacultyMapper;
import com.hau.ketnguyen.it.service.mapper.StudentMapper;
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
public class ClassServiceImpl implements ClassService {
    private final ClassReps classReps;
    private final ClassMapper classMapper;
    private final FacultyReps facultyReps;
    private final FacultyMapper facultyMapper;
    private final StudentReps studentReps;
    private final StudentMapper studentMapper;

    @Override
    public ClassDTO save(ClassDTO classDTO) {
        return classMapper.to(classReps.save(classMapper.from(classDTO)));
    }

    @Override
    public ClassDTO edit(Long id, ClassDTO classDTO) {
        Optional<Classes> classesOptional = classReps.findById(id);

        if (classesOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy lớp");
        }

        Classes classes = classesOptional.get();
        BeanUtil.copyNonNullProperties(classDTO, classes);

        return classMapper.to(classes);
    }

    @Override
    public void delete(Long id) {
        Optional<Classes> classesOptional = classReps.findById(id);

        if (classesOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy lớp");
        }

        classReps.delete(classesOptional.get());
    }

    @Override
    public ClassDTO findById(Long id) {
        Optional<Classes> classesOptional = classReps.findById(id);

        if (classesOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy lớp");
        }

        ClassDTO classDTO = classMapper.to(classesOptional.get());
        List<StudentDTO> stds = studentReps.findByClassIdIn(Collections.singletonList(classDTO.getId()))
                .stream().map(studentMapper::to).collect(Collectors.toList());
        List<FacultyDTO> facultyDTOS = facultyReps.findByIdIn(Collections.singletonList(classDTO.getFacultyId()))
                .stream().map(facultyMapper::to).collect(Collectors.toList());
        classDTO.setStds(stds);
        classDTO.setFacultyDTO(facultyDTOS.get(0));

        return classDTO;
    }

    @Override
    public PageDataResponse<ClassDTO> getAll(SearchClassRequest request)  {
        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        Page<ClassDTO> page = classReps.search(request, pageable).map(classMapper::to);

        if (!page.isEmpty()) {
            List<Long> classIds = page.map(ClassDTO::getId).toList();
            List<Long> facultyIds = page.map(ClassDTO::getFacultyId).toList();

            if (!classIds.isEmpty() && !facultyIds.isEmpty()) {
                List<StudentDTO> stds = new ArrayList<>();

                List<StudentDTO> listStd = studentReps.findByClassIdIn(classIds)
                        .stream().map(studentMapper::to).collect(Collectors.toList());
                Map<Long, FacultyDTO> facultyDTOS = facultyReps.findByIdIn(facultyIds)
                        .stream().map(facultyMapper::to).collect(Collectors.toMap(FacultyDTO::getId, f -> f));

                page.forEach(p -> {
                    if (!listStd.isEmpty()) {
                        listStd.forEach(std -> {
                            if (Objects.equals(p.getId(), std.getClassId())) {
                                stds.add(std);
                            }
                        });

                        p.setStds(stds);
                    }

                    if (!facultyDTOS.isEmpty() && facultyDTOS.containsKey(p.getFacultyId())) {
                        p.setFacultyDTO(facultyDTOS.get(p.getFacultyId()));
                    }
                });
            }
        }

        return PageDataResponse.of(page);
    }
}
