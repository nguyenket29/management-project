package com.hau.ketnguyen.it.service.mapper;

import com.hau.ketnguyen.it.entity.hau.Comments;
import com.hau.ketnguyen.it.model.dto.hau.CommentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper extends EntityMapper<CommentDTO, Comments> {
}
