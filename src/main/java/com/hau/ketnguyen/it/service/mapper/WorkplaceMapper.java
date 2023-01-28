package com.hau.ketnguyen.it.service.mapper;

import com.hau.ketnguyen.it.entity.hau.Workplaces;
import com.hau.ketnguyen.it.model.dto.excel.FacultyExcelDTO;
import com.hau.ketnguyen.it.model.dto.excel.WorkplaceExcelDTO;
import com.hau.ketnguyen.it.model.dto.hau.FacultyDTO;
import com.hau.ketnguyen.it.model.dto.hau.WorkplaceDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkplaceMapper extends EntityMapper<WorkplaceDTO, Workplaces> {
    List<WorkplaceExcelDTO> toExcel(List<WorkplaceDTO> dto);
}
