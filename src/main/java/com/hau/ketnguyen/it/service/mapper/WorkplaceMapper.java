package com.hau.ketnguyen.it.service.mapper;

import com.hau.ketnguyen.it.entity.hau.Workplaces;
import com.hau.ketnguyen.it.model.dto.hau.WorkplaceDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkplaceMapper extends EntityMapper<WorkplaceDTO, Workplaces> {
}
