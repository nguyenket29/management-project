package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.Classes;
import com.hau.ketnguyen.it.entity.hau.Faculties;
import com.hau.ketnguyen.it.model.request.hau.SearchFacultyRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyReps extends CrudRepository<Faculties, Long> {
    @Query("SELECT c FROM faculties c " +
            "WHERE (:#{#request.code} IS NULL OR lower(c.code) LIKE %:#{#request.code}%) " +
            " AND (:#{#request.name} IS NULL OR lower(c.name) LIKE %:#{#request.name}%) " +
            " AND (:#{#request.specialization} IS NULL OR lower(c.specialization) LIKE %:#{#request.specialization}%) " +
            " AND (:#{#request.workplaceId} IS NULL OR c.workplaceId = :#{#request.workplaceId}) " +
            " ORDER BY c.id desc")
    Page<Faculties> search(SearchFacultyRequest request, Pageable pageable);
    List<Faculties> findByIdIn(List<Long> ids);
    Optional<Faculties> findByCodeOrName(String code, String name);
}
