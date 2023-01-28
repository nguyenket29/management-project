package com.hau.ketnguyen.it.service.mapper;

import com.hau.ketnguyen.it.entity.hau.Faculties;
import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import com.hau.ketnguyen.it.model.dto.excel.FacultyExcelDTO;
import com.hau.ketnguyen.it.model.dto.excel.UserExcelDTO;
import com.hau.ketnguyen.it.model.dto.hau.FacultyDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FacultyMapper extends EntityMapper<FacultyDTO, Faculties> {
    List<FacultyExcelDTO> toExcel(List<FacultyDTO> dto);
}
