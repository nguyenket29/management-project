package com.hau.ketnguyen.it.service.mapper;

import com.hau.ketnguyen.it.entity.hau.Faculties;
import com.hau.ketnguyen.it.model.dto.hau.FacultyDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FacultyMapper extends EntityMapper<FacultyDTO, Faculties> {
}
