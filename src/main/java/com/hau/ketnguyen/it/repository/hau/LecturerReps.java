package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.Lecturers;
import com.hau.ketnguyen.it.model.request.hau.SearchLecturerRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface LecturerReps extends CrudRepository<Lecturers, Long> {
    @Query("SELECT l FROM lecturers l " +
            "LEFT JOIN User u ON l.userId = u.id " +
            "LEFT JOIN UserInfo ui ON l.userInfoId = ui.id " +
            "WHERE (:#{#request.degree} IS NULL OR lower(l.degree) LIKE %:#{#request.degree}%) " +
            " AND (:#{#request.regency} IS NULL OR lower(l.regency) LIKE %:#{#request.regency}%) " +
            " AND (:#{#request.codeLecture} IS NULL OR lower(l.codeLecture) = :#{#request.codeLecture}) " +
            " AND (:#{#request.fullName} IS NULL OR lower(ui.fullName) LIKE %:#{#request.fullName}%) " +
            " AND (:#{#request.email} IS NULL OR lower(u.email) LIKE %:#{#request.email}%) " +
            " AND (:#{#request.phoneNumber} IS NULL OR lower(ui.phoneNumber) = :#{#request.phoneNumber}) " +
            " AND (:#{#request.town} IS NULL OR lower(ui.town) LIKE %:#{#request.town}%) " +
            " AND (:#{#request.address} IS NULL OR lower(ui.address) LIKE %:#{#request.address}%) " +
            " AND (:#{#request.facultyId} IS NULL OR l.facultyId = :#{#request.facultyId}) " +
            " AND (:#{#request.dateOfBirth} IS NULL OR ui.dateOfBirth = :#{#request.dateOfBirth}) " +
            " AND (:#{#request.gender} IS NULL OR ui.gender = :#{#request.gender}) " +
            " ORDER BY l.id desc")
    Page<Lecturers> search(SearchLecturerRequest request, Pageable pageable);

    List<Lecturers> findByIdIn(List<Long> ids);
    Optional<Lecturers> findByUserId(Integer userId);
    List<Lecturers> findByUserIdIn(List<Integer> userIds);
}
