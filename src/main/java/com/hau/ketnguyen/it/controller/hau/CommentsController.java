package com.hau.ketnguyen.it.controller.hau;

import com.hau.ketnguyen.it.model.dto.hau.CommentDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchCommentRequest;
import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.CommentService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "Comments Controller", description = "Các APIs quản lý bình luận")
@RequestMapping("/comments")
@AllArgsConstructor
@Slf4j
public class CommentsController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<APIResponse<CommentDTO>> save(@RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(APIResponse.success(commentService.save(commentDTO)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<CommentDTO>> edit(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(APIResponse.success(commentService.edit(id, commentDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> delete(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.ok(APIResponse.success());
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<CommentDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(APIResponse.success(commentService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<APIResponse<PageDataResponse<CommentDTO>>> getAll(SearchCommentRequest request) {
        return ResponseEntity.ok(APIResponse.success(commentService.getAll(request)));
    }
}
