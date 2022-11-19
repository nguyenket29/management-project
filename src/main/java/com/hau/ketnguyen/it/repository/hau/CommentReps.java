package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.Comments;
import com.hau.ketnguyen.it.model.request.hau.SearchCommentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentReps extends CrudRepository<Comments, Long> {
    @Query("SELECT c FROM comments c " +
            "WHERE (:#{#request.userId} IS NULL OR c.userId = :#{#request.userId}) " +
            " AND (:#{#request.topicId} IS NULL OR c.topicId = :#{#request.topicId}) " +
            " ORDER BY c.id desc")
    Page<Comments> search(SearchCommentRequest request, Pageable pageable);
}
