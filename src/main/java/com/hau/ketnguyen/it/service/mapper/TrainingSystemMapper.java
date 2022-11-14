package com.hau.ketnguyen.it.service.mapper;

import com.hau.ketnguyen.it.entity.hau.TrainingSystem;
import com.hau.ketnguyen.it.model.dto.hau.TrainingSystemDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainingSystemMapper extends EntityMapper<TrainingSystemDTO, TrainingSystem> {
}
