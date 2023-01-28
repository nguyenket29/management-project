package com.hau.ketnguyen.it.service.mapper;

import com.hau.ketnguyen.it.entity.hau.Topics;
import com.hau.ketnguyen.it.model.dto.excel.TopicExcelDTO;
import com.hau.ketnguyen.it.model.dto.hau.TopicDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TopicMapper extends EntityMapper<TopicDTO, Topics> {
    List<TopicExcelDTO> toExcel(List<TopicDTO> dto);
}
