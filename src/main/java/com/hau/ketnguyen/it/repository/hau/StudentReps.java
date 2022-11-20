package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.Lecturers;
import com.hau.ketnguyen.it.entity.hau.Specialize;
import com.hau.ketnguyen.it.entity.hau.Students;
import com.hau.ketnguyen.it.model.request.hau.SearchLecturerRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchSpecializeRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchStudentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentReps extends CrudRepository<Students, Long> {
    List<Students> findByClassIdIn(List<Long> classIds);

    @Query("SELECT l FROM students l, User u " +
            "WHERE l.userId = u.id " +
            " AND (:#{#request.lastName} IS NULL OR u.lastName LIKE %:#{#request.lastName}%) " +
            " AND (:#{#request.firstName} IS NULL OR u.firstName LIKE %:#{#request.firstName}%) " +
            " AND (:#{#request.email} IS NULL OR u.email LIKE %:#{#request.email}%) " +
            " AND (:#{#request.phoneNumber} IS NULL OR u.phoneNumber LIKE %:#{#request.phoneNumber}%) " +
            " AND (:#{#request.address} IS NULL OR u.address LIKE %:#{#request.address}%) " +
            " AND (:#{#request.phoneNumber} IS NULL OR u.phoneNumber LIKE %:#{#request.phoneNumber}%) " +
            " AND (:#{#request.topicId} IS NULL OR l.topicId = :#{#request.topicId}) " +
            " AND (:#{#request.classId} IS NULL OR l.classId = :#{#request.classId}) " +
            " AND (:#{#request.dateOfBirth} IS NULL OR u.birthday = :#{#request.dateOfBirth}) " +
            " AND (:#{#request.gender} IS NULL OR u.gender = :#{#request.gender}) " +
            " ORDER BY l.id desc")
    Page<Students> search(SearchStudentRequest request, Pageable pageable);
}
