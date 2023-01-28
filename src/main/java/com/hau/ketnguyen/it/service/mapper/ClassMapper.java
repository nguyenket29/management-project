package com.hau.ketnguyen.it.service.mapper;

import com.hau.ketnguyen.it.entity.hau.Classes;
import com.hau.ketnguyen.it.model.dto.excel.CategoryExcelDTO;
import com.hau.ketnguyen.it.model.dto.excel.ClassExcelDTO;
import com.hau.ketnguyen.it.model.dto.excel.LectureExcelDTO;
import com.hau.ketnguyen.it.model.dto.hau.CategoryDTO;
import com.hau.ketnguyen.it.model.dto.hau.ClassDTO;
import com.hau.ketnguyen.it.model.dto.hau.LecturerDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClassMapper extends EntityMapper<ClassDTO, Classes> {
    List<ClassExcelDTO> toExcel(List<ClassDTO> dto);
}
