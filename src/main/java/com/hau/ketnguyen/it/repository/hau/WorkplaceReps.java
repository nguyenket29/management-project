package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.Workplaces;
import com.hau.ketnguyen.it.model.request.hau.SearchWorkplaceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkplaceReps extends CrudRepository<Workplaces, Long> {
    @Query("SELECT c FROM workplaces c " +
            "WHERE (:#{#request.address} IS NULL OR c.address LIKE %:#{#request.address}%) " +
            " AND (:#{#request.name} IS NULL OR c.name LIKE %:#{#request.name}%) " +
            " AND (:#{#request.phoneNumber} IS NULL OR c.phoneNumber LIKE %:#{#request.phoneNumber}%) " +
            " AND (:#{#request.email} IS NULL OR c.email LIKE %:#{#request.email}%) " +
            " ORDER BY c.id desc")
    Page<Workplaces> search(SearchWorkplaceRequest request, Pageable pageable);
}
