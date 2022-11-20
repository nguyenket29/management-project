package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.Lecturers;
import com.hau.ketnguyen.it.entity.hau.Specialize;
import com.hau.ketnguyen.it.model.request.hau.SearchLecturerRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchSpecializeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecializeReps extends CrudRepository<Specialize, Long> {
    List<Specialize> findByIdIn(List<Long> ids);

    @Query("SELECT l FROM specialize l " +
            "WHERE (:#{#request.name} IS NULL OR l.name LIKE %:#{#request.name}%) " +
            " AND (:#{#request.facultyId} IS NULL OR l.facultyId = :#{#request.facultyId}) " +
            " AND (:#{#request.trainSystemId} IS NULL OR l.trainSystemId = :#{#request.trainSystemId}) " +
            " ORDER BY l.id desc")
    Page<Specialize> search(SearchSpecializeRequest request, Pageable pageable);
}
