package com.hau.ketnguyen.it.service.mapper;

import com.hau.ketnguyen.it.entity.hau.Students;
import com.hau.ketnguyen.it.model.dto.excel.CategoryExcelDTO;
import com.hau.ketnguyen.it.model.dto.excel.StudentExcelDTO;
import com.hau.ketnguyen.it.model.dto.hau.CategoryDTO;
import com.hau.ketnguyen.it.model.dto.hau.StudentDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper extends EntityMapper<StudentDTO, Students> {
    List<StudentExcelDTO> toExcel(List<StudentDTO> dto);
}
