package com.hau.ketnguyen.it.controller.hau;

import com.hau.ketnguyen.it.controller.APIController;
import com.hau.ketnguyen.it.model.dto.hau.ClassDTO;
import com.hau.ketnguyen.it.model.dto.hau.CommentDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchClassRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchCommentRequest;
import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.ClassService;
import com.hau.ketnguyen.it.service.CommentService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Comments Controller", description = "Các APIs quản lý bình luận")
@RequestMapping("/comments")
@AllArgsConstructor
@Slf4j
public class CommentsController extends APIController<CommentDTO, SearchCommentRequest> {
    private final CommentService commentService;

    @Override
    public ResponseEntity<APIResponse<CommentDTO>> save(@RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(APIResponse.success(commentService.save(commentDTO)));
    }

    @Override
    public ResponseEntity<APIResponse<CommentDTO>> edit(Long id,@RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(APIResponse.success(commentService.edit(id, commentDTO)));
    }

    @Override
    public ResponseEntity<APIResponse<Void>> delete(Long id) {
        commentService.delete(id);
        return ResponseEntity.ok(APIResponse.success());
    }

    @Override
    public ResponseEntity<APIResponse<CommentDTO>> findById(Long id) {
        return ResponseEntity.ok(APIResponse.success(commentService.findById(id)));
    }

    @Override
    public ResponseEntity<APIResponse<PageDataResponse<CommentDTO>>> getAll(SearchCommentRequest request) {
        return ResponseEntity.ok(APIResponse.success(commentService.getAll(request)));
    }
}
