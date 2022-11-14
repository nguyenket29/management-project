package com.hau.ketnguyen.it.service.mapper;

import com.hau.ketnguyen.it.entity.hau.Specialize;
import com.hau.ketnguyen.it.model.dto.hau.SpecializeDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpecializeMapper extends EntityMapper<SpecializeDTO, Specialize> {
}
