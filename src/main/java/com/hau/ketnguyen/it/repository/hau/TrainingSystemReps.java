package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.Specialize;
import com.hau.ketnguyen.it.entity.hau.TrainingSystem;
import com.hau.ketnguyen.it.model.request.hau.SearchSpecializeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingSystemReps extends CrudRepository<TrainingSystem, Long> {
    List<TrainingSystem> findByIdIn(List<Long> ids);

    @Query("SELECT l FROM training_system l " +
            "WHERE (:#{#request.name} IS NULL OR l.name LIKE %:#{#request.name}%) " +
            " AND (:#{#request.code} IS NULL OR l.code LIKE %:#{#request.facultyId}%) " +
            " ORDER BY l.id desc")
    Page<Specialize> search(SearchSpecializeRequest request, Pageable pageable);
}
