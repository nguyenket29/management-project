package com.hau.ketnguyen.it.service;

import com.hau.ketnguyen.it.entity.hau.Comments;
import com.hau.ketnguyen.it.model.dto.hau.CommentDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchCommentRequest;

import java.util.List;

public interface CommentService extends GenericService<CommentDTO, SearchCommentRequest> {
    List<Comments> findAll();
}
