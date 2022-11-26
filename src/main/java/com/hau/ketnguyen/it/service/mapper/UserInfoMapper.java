package com.hau.ketnguyen.it.service.mapper;

import com.hau.ketnguyen.it.entity.hau.UserInfo;
import com.hau.ketnguyen.it.model.dto.auth.UserInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserInfoMapper {
    UserInfoDTO to(UserInfo entity);

    @Mapping(target = "id", ignore = true)
    UserInfo from(UserInfoDTO dto);

    @Mapping(target = "id", ignore = true)
    UserInfo copy(UserInfoDTO dto, @MappingTarget UserInfo entity);
}
