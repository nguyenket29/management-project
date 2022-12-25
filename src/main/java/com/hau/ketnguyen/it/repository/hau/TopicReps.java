package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.Topics;
import com.hau.ketnguyen.it.model.request.hau.SearchTopicRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicReps extends CrudRepository<Topics, Long> {
    List<Topics> findByIdIn(List<Long> ids);
    @Query("SELECT c FROM topics c " +
            "WHERE (:#{#request.lecturerId} IS NULL OR c.lecturerId = :#{#request.lecturerId}) " +
            " AND (:#{#request.name} IS NULL OR c.name LIKE %:#{#request.name}%) " +
            " AND (:#{#request.year} IS NULL OR c.year = :#{#request.year}) " +
            " ORDER BY c.id desc")
    Page<Topics> search(SearchTopicRequest request, Pageable pageable);

    List<Topics> findByStatus(boolean status);
}
