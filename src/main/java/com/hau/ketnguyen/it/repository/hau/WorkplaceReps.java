package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.Workplaces;
import com.hau.ketnguyen.it.model.request.hau.SearchWorkplaceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkplaceReps extends CrudRepository<Workplaces, Long> {
    @Query("SELECT c FROM workplaces c " +
            "WHERE (:#{#request.address} IS NULL OR lower(c.address) LIKE %:#{#request.address}%) " +
            " AND (:#{#request.name} IS NULL OR lower(c.name) LIKE %:#{#request.name}%) " +
            " AND (:#{#request.phoneNumber} IS NULL OR lower(c.phoneNumber) LIKE %:#{#request.phoneNumber}%) " +
            " AND (:#{#request.email} IS NULL OR lower(c.email) LIKE %:#{#request.email}%) " +
            " ORDER BY c.id desc")
    Page<Workplaces> search(SearchWorkplaceRequest request, Pageable pageable);

    List<Workplaces> findByIdIn(List<Long> ids);
}
