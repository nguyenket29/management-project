package com.hau.ketnguyen.it.service.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

public interface EntityMapper<D, E> {
    @Mapping(target = "id", ignore = true)
    E from(D dto);

    D to(E entity);

    @Mapping(target = "id", ignore = true)
    E copy(D dto, @MappingTarget E entity);

    List<E> toEntity(List<D> dtoList);

    List<D> toDto(List<E> entityList);
}
