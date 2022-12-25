package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.BeanUtil;
import com.hau.ketnguyen.it.common.util.PageableUtils;
import com.hau.ketnguyen.it.entity.auth.User;
import com.hau.ketnguyen.it.entity.hau.Comments;
import com.hau.ketnguyen.it.entity.hau.Topics;
import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import com.hau.ketnguyen.it.model.dto.hau.CommentDTO;
import com.hau.ketnguyen.it.model.dto.hau.TopicDTO;
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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentReps commentReps;
    private final CommentMapper commentMapper;
    private final UserReps userReps;
    private final TopicReps topicReps;
    private final UserMapper userMapper;
    private final TopicMapper topicMapper;

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
        BeanUtil.copyNonNullProperties(commentDTO, comments);

        return commentMapper.to(commentReps.save(comments));
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

        User user = userReps.findById(Integer.parseInt(String.valueOf(commentsOptional.get().getUserId())))
                .orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy người dùng"));
        UserDTO userDTO = userMapper.to(user);
        Topics topics = topicReps.findById(commentsOptional.get().getTopicId())
                .orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy đề tài"));
        TopicDTO topicDTO = topicMapper.to(topics);
        CommentDTO commentDTO = commentMapper.to(commentsOptional.get());
        commentDTO.setUserDTO(userDTO);
        commentDTO.setTopicDTO(topicDTO);

        return commentDTO;
    }

    @Override
    public PageDataResponse<CommentDTO> getAll(SearchCommentRequest request) {
        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        Page<CommentDTO> page = commentReps.search(request, pageable).map(commentMapper::to);
        if (!page.isEmpty()) {
            List<Long> userIdLongs = page.map(CommentDTO::getUserId).toList();
            List<Integer> userIdInts = userIdLongs.stream().map(Long::intValue).collect(Collectors.toList());
            List<Long> topicIds = page.map(CommentDTO::getTopicId).toList();

            Map<Integer, UserDTO> userDTOS = userReps.findByIds(userIdInts)
                    .stream().map(userMapper::to).collect(Collectors.toMap(UserDTO::getId, u -> u));
            Map<Long, TopicDTO> topicDTOS = topicReps.findByIdIn(topicIds)
                    .stream().map(topicMapper::to).collect(Collectors.toMap(TopicDTO::getId, t -> t));

            page.forEach(p -> {
                if (!userDTOS.isEmpty() && userDTOS.containsKey(p.getUserId().intValue())) {
                    p.setUserDTO(userDTOS.get(p.getUserId().intValue()));
                }

                if (!topicDTOS.isEmpty() && topicDTOS.containsKey(p.getTopicId())) {
                    p.setTopicDTO(topicDTOS.get(p.getTopicId()));
                }
            });
        }

        return PageDataResponse.of(page);
    }

    public void sendCommentWebsocket() {

    }
}
