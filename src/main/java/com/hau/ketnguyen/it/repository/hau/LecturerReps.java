package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.Lecturers;
import com.hau.ketnguyen.it.model.request.hau.SearchLecturerRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LecturerReps extends CrudRepository<Lecturers, Long> {
    @Query("SELECT l FROM lecturers l " +
            "LEFT JOIN User u ON l.userId = u.id " +
            "LEFT JOIN UserInfo ui ON l.userId = ui.id " +
            "WHERE (:#{#request.degree} IS NULL OR l.degree LIKE %:#{#request.degree}%) " +
            " AND (:#{#request.regency} IS NULL OR l.regency LIKE %:#{#request.regency}%) " +
            " AND (:#{#request.fullName} IS NULL OR ui.fullName LIKE %:#{#request.fullName}%) " +
            " AND (:#{#request.email} IS NULL OR u.email LIKE %:#{#request.email}%) " +
            " AND (:#{#request.phoneNumber} IS NULL OR ui.phoneNumber LIKE %:#{#request.phoneNumber}%) " +
            " AND (:#{#request.address} IS NULL OR ui.address LIKE %:#{#request.address}%) " +
            " AND (:#{#request.facultyId} IS NULL OR l.facultyId = :#{#request.facultyId}) " +
            " AND (:#{#request.workplaceId} IS NULL OR l.workplaceId = :#{#request.workplaceId}) " +
            " AND (:#{#request.dateOfBirth} IS NULL OR ui.dateOfBirth = :#{#request.dateOfBirth}) " +
            " AND (:#{#request.gender} IS NULL OR ui.gender = :#{#request.gender}) " +
            " ORDER BY l.id desc")
    Page<Lecturers> search(SearchLecturerRequest request, Pageable pageable);

    List<Lecturers> findByIdIn(List<Long> ids);
}
