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
    @Query("SELECT l FROM lecturers l, User u " +
            "WHERE l.userId = u.id " +
            " AND (:#{#request.degree} IS NULL OR l.degree LIKE %:#{#request.degree}%) " +
            " AND (:#{#request.regency} IS NULL OR l.regency LIKE %:#{#request.regency}%) " +
            " AND (:#{#request.lastName} IS NULL OR u.lastName LIKE %:#{#request.lastName}%) " +
            " AND (:#{#request.firstName} IS NULL OR u.firstName LIKE %:#{#request.firstName}%) " +
            " AND (:#{#request.email} IS NULL OR u.email LIKE %:#{#request.email}%) " +
            " AND (:#{#request.phoneNumber} IS NULL OR u.phoneNumber LIKE %:#{#request.phoneNumber}%) " +
            " AND (:#{#request.address} IS NULL OR u.address LIKE %:#{#request.address}%) " +
            " AND (:#{#request.phoneNumber} IS NULL OR u.phoneNumber LIKE %:#{#request.phoneNumber}%) " +
            " AND (:#{#request.facultyId} IS NULL OR l.facultyId = :#{#request.facultyId}) " +
            " AND (:#{#request.workplaceId} IS NULL OR l.workplaceId = :#{#request.workplaceId}) " +
            " AND (:#{#request.dateOfBirth} IS NULL OR u.birthday = :#{#request.dateOfBirth}) " +
            " AND (:#{#request.gender} IS NULL OR u.gender = :#{#request.gender}) " +
            " ORDER BY l.id desc")
    Page<Lecturers> search(SearchLecturerRequest request, Pageable pageable);

    List<Lecturers> findByIdIn(List<Long> ids);
}
