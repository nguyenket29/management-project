package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.PageableUtils;
import com.hau.ketnguyen.it.entity.auth.User;
import com.hau.ketnguyen.it.entity.hau.Comments;
import com.hau.ketnguyen.it.model.dto.hau.CommentDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchCommentRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.repository.auth.UserReps;
import com.hau.ketnguyen.it.repository.hau.AssemblyReps;
import com.hau.ketnguyen.it.repository.hau.CommentReps;
import com.hau.ketnguyen.it.repository.hau.TopicReps;
import com.hau.ketnguyen.it.service.CommentService;
import com.hau.ketnguyen.it.service.mapper.AssemblyMapper;
import com.hau.ketnguyen.it.service.mapper.CommentMapper;
import com.hau.ketnguyen.it.service.mapper.TopicMapper;
import com.hau.ketnguyen.it.service.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentReps commentReps;
    private final CommentMapper commentMapper;
    private final UserReps userReps;
    private final TopicReps topicReps;
    private final AssemblyReps assemblyReps;
    private final UserMapper userMapper;
    private final TopicMapper topicMapper;
    private final AssemblyMapper assemblyMapper;

    @Override
    public CommentDTO save(CommentDTO commentDTO) {
        return commentMapper.to(commentReps.save(commentMapper.from(commentDTO)));
    }

    @Override
    public CommentDTO edit(Long id, CommentDTO commentDTO) {
        Optional<Comments> commentsOptional = commentReps.findById(id);

        if (commentsOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy bình luận");
        }

        Comments comments = commentsOptional.get();
        commentMapper.copy(commentDTO, comments);

        return commentMapper.to(comments);
    }

    @Override
    public void delete(Long id) {
        Optional<Comments> commentsOptional = commentReps.findById(id);

        if (commentsOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy bình luận");
        }

        commentReps.delete(commentsOptional.get());
    }

    @Override
    public CommentDTO findById(Long id) {
        Optional<Comments> commentsOptional = commentReps.findById(id);

        if (commentsOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy bình luận");
        }

        return commentMapper.to(commentsOptional.get());
    }

    @Override
    public PageDataResponse<CommentDTO> getAll(SearchCommentRequest request) {
        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        Page<CommentDTO> page = commentReps.search(request, pageable).map(commentMapper::to);
        return PageDataResponse.of(page);
    }
}
