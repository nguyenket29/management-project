package com.hau.ketnguyen.it.service.mapper;

import com.hau.ketnguyen.it.entity.hau.Categories;
import com.hau.ketnguyen.it.model.dto.excel.CategoryExcelDTO;
import com.hau.ketnguyen.it.model.dto.hau.CategoryDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Categories> {
    List<CategoryExcelDTO> toExcel(List<CategoryDTO> dto);
}
