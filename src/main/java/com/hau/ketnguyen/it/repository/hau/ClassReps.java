package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.Classes;
import com.hau.ketnguyen.it.entity.hau.Files;
import com.hau.ketnguyen.it.entity.hau.Workplaces;
import com.hau.ketnguyen.it.model.request.hau.SearchClassRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchWorkplaceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassReps extends CrudRepository<Classes, Long> {
    @Query("SELECT c FROM classes c " +
            "WHERE (:#{#request.code} IS NULL OR c.code LIKE %:#{#request.code}%) " +
            " AND (:#{#request.name} IS NULL OR c.name LIKE %:#{#request.name}%) " +
            " AND (:#{#request.facultyId} IS NULL OR c.facultyId = :#{#request.facultyId}) " +
            " ORDER BY c.id desc")
    Page<Classes> search(SearchClassRequest request, Pageable pageable);
}
