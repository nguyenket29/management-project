package com.hau.ketnguyen.it.service.mapper;

import com.hau.ketnguyen.it.entity.hau.Assemblies;
import com.hau.ketnguyen.it.model.dto.excel.AssemblyExcelDTO;
import com.hau.ketnguyen.it.model.dto.excel.CategoryExcelDTO;
import com.hau.ketnguyen.it.model.dto.hau.AssemblyDTO;
import com.hau.ketnguyen.it.model.dto.hau.CategoryDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AssemblyMapper extends EntityMapper<AssemblyDTO, Assemblies> {
    List<AssemblyExcelDTO> toExcel(List<AssemblyDTO> dto);
}
