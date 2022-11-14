package com.hau.ketnguyen.it.service.mapper;

import com.hau.ketnguyen.it.entity.hau.Students;
import com.hau.ketnguyen.it.model.dto.hau.StudentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper extends EntityMapper<StudentDTO, Students> {
}
