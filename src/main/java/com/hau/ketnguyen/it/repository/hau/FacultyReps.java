package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.Faculties;
import com.hau.ketnguyen.it.model.request.hau.SearchFacultyRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacultyReps extends CrudRepository<Faculties, Long> {
    @Query("SELECT c FROM faculties c " +
            "WHERE (:#{#request.code} IS NULL OR c.code LIKE %:#{#request.code}%) " +
            " AND (:#{#request.name} IS NULL OR c.name LIKE %:#{#request.name}%) " +
            " AND (:#{#request.specialization} IS NULL OR c.specialization LIKE %:#{#request.specialization}%) " +
            " ORDER BY c.id desc")
    Page<Faculties> search(SearchFacultyRequest request, Pageable pageable);
    List<Faculties> findByIdIn(List<Long> ids);
}
