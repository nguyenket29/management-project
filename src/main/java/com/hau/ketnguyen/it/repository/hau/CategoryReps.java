package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.Categories;
import com.hau.ketnguyen.it.model.request.hau.SearchCategoryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryReps extends CrudRepository<Categories, Long> {
    @Query("SELECT c FROM categories c " +
            "WHERE (:#{#request.code} IS NULL OR lower(c.code) LIKE %:#{#request.code}%) " +
            " AND (:#{#request.name} IS NULL OR lower(c.name) LIKE %:#{#request.name}%) " +
            " AND (:#{#request.description} IS NULL OR lower(c.description) LIKE %:#{#request.description}%) " +
            " ORDER BY c.id desc")
    Page<Categories> search(SearchCategoryRequest request, Pageable pageable);

    List<Categories> findByIdIn(List<Long> categoryIds);
}
