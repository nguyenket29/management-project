package com.hau.ketnguyen.it.service.mapper;

import com.hau.ketnguyen.it.entity.hau.Lecturers;
import com.hau.ketnguyen.it.model.dto.excel.CategoryExcelDTO;
import com.hau.ketnguyen.it.model.dto.excel.LectureExcelDTO;
import com.hau.ketnguyen.it.model.dto.hau.CategoryDTO;
import com.hau.ketnguyen.it.model.dto.hau.LecturerDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LecturerMapper extends EntityMapper<LecturerDTO, Lecturers> {
    List<LectureExcelDTO> toExcel(List<LecturerDTO> dto);
}
