package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.Assemblies;
import com.hau.ketnguyen.it.entity.hau.Files;
import com.hau.ketnguyen.it.entity.hau.Lecturers;
import com.hau.ketnguyen.it.model.request.hau.SearchAssemblyRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchLecturerRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssemblyReps extends CrudRepository<Assemblies, Long> {
    @Query("SELECT l FROM assembly l " +
            " WHERE (:#{#request.topicId} IS NULL OR l.topicId = :#{#request.topicId}) " +
            " AND (:#{#request.nameAssembly} IS NULL OR lower(l.nameAssembly) LIKE %:#{#request.nameAssembly}%) " +
            " AND (:#{#request.lecturePresidentId} IS NULL OR l.lecturePresidentId = :#{#request.lecturePresidentId}) " +
            " AND (:#{#request.lectureSecretaryId} IS NULL OR l.lectureSecretaryId = :#{#request.lectureSecretaryId}) " +
            " ORDER BY l.id desc")
    Page<Assemblies> search(SearchAssemblyRequest request, Pageable pageable);
    List<Assemblies> findByLecturePresidentIdIn(List<Long> lecturePresidentIds);
}
