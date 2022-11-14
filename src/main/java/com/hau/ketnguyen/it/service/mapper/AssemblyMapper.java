package com.hau.ketnguyen.it.service.mapper;

import com.hau.ketnguyen.it.entity.hau.Assemblies;
import com.hau.ketnguyen.it.model.dto.hau.AssemblyDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssemblyMapper extends EntityMapper<AssemblyDTO, Assemblies> {
}
