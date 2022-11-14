package com.hau.ketnguyen.it.service.mapper;

import com.hau.ketnguyen.it.entity.hau.Classes;
import com.hau.ketnguyen.it.model.dto.hau.ClassDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClassMapper extends EntityMapper<ClassDTO, Classes> {
}
