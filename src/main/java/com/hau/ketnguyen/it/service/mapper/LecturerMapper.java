package com.hau.ketnguyen.it.service.mapper;

import com.hau.ketnguyen.it.entity.hau.Lecturers;
import com.hau.ketnguyen.it.model.dto.hau.LecturerDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LecturerMapper extends EntityMapper<LecturerDTO, Lecturers> {
}
