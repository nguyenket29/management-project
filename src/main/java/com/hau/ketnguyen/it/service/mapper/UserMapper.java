package com.hau.ketnguyen.it.service.mapper;

import com.hau.ketnguyen.it.entity.auth.User;
import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import com.hau.ketnguyen.it.model.dto.excel.CategoryExcelDTO;
import com.hau.ketnguyen.it.model.dto.excel.UserExcelDTO;
import com.hau.ketnguyen.it.model.dto.hau.CategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO to(User entity);

    @Mapping(target = "id", ignore = true)
    User from(UserDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    User copy(UserDTO dto, @MappingTarget User entity);

    List<UserExcelDTO> toExcel(List<UserDTO> dto);
}
