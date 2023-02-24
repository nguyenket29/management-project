package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.Students;
import com.hau.ketnguyen.it.model.request.hau.SearchStudentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentReps extends CrudRepository<Students, Long> {
    Optional<Students> findByUserId(Integer userId);
    List<Students> findByClassIdIn(List<Long> classIds);
    List<Students> findByUserIdIn(List<Integer> userIds);
    List<Students> findByIdIn(List<Long> studentIds);
    Optional<Students> findByTopicId(Long topicId);
    Optional<Students> findByTopicIdAndId(Long topicId, Long id);
    @Query("SELECT l FROM students l " +
            "LEFT JOIN User u ON l.userId = u.id " +
            "LEFT JOIN UserInfo ui ON l.userInfoId = ui.id " +
            "WHERE (:#{#request.fullName} IS NULL OR lower(ui.fullName) LIKE %:#{#request.fullName}%) " +
            " AND (:#{#request.email} IS NULL OR lower(u.email) LIKE %:#{#request.email}%) " +
            " AND (:#{#request.codeStudent} IS NULL OR lower(l.codeStudent) = :#{#request.codeStudent}) " +
            " AND (:#{#request.stdPass} IS NULL OR l.stdPass = :#{#request.stdPass}) " +
            " AND (:#{#request.phoneNumber} IS NULL OR lower(ui.phoneNumber) = :#{#request.phoneNumber}) " +
            " AND (:#{#request.address} IS NULL OR lower(ui.address) LIKE %:#{#request.address}%) " +
            " AND (:#{#request.town} IS NULL OR lower(ui.town) LIKE %:#{#request.town}%) " +
            " AND (:#{#request.topicId} IS NULL OR l.topicId = :#{#request.topicId}) " +
            " AND (:#{#request.classId} IS NULL OR l.classId = :#{#request.classId}) " +
            " AND (:#{#request.dateOfBirth} IS NULL OR ui.dateOfBirth = :#{#request.dateOfBirth}) " +
            " AND (:#{#request.gender} IS NULL OR ui.gender = :#{#request.gender}) " +
            " ORDER BY l.id desc")
    Page<Students> search(SearchStudentRequest request, Pageable pageable);

    @Query("SELECT l FROM students l " +
            "LEFT JOIN User u ON l.userId = u.id " +
            "LEFT JOIN UserInfo ui ON l.userInfoId = ui.id " +
            "WHERE (:#{#request.fullName} IS NULL OR lower(ui.fullName) LIKE %:#{#request.fullName}%) " +
            " AND (:#{#request.email} IS NULL OR lower(u.email) LIKE %:#{#request.email}%) " +
            " AND (l.topicId IN :topicIds) " +
            " AND (:#{#request.codeStudent} IS NULL OR lower(l.codeStudent) = :#{#request.codeStudent}) " +
            " AND (:#{#request.stdPass} IS NULL OR l.stdPass = :#{#request.stdPass}) " +
            " AND (:#{#request.phoneNumber} IS NULL OR lower(ui.phoneNumber) = :#{#request.phoneNumber}) " +
            " AND (:#{#request.address} IS NULL OR lower(ui.address) LIKE %:#{#request.address}%) " +
            " AND (:#{#request.town} IS NULL OR lower(ui.town) LIKE %:#{#request.town}%) " +
            " AND (:#{#request.topicId} IS NULL OR l.topicId = :#{#request.topicId}) " +
            " AND (:#{#request.classId} IS NULL OR l.classId = :#{#request.classId}) " +
            " AND (:#{#request.dateOfBirth} IS NULL OR ui.dateOfBirth = :#{#request.dateOfBirth}) " +
            " AND (:#{#request.gender} IS NULL OR ui.gender = :#{#request.gender}) " +
            " ORDER BY l.id desc")
    Page<Students> search(SearchStudentRequest request, List<Long> topicIds, Pageable pageable);
    List<Students> findByStdPass(boolean stdPass);
    Optional<Students> findByCodeStudent(String code);
}
