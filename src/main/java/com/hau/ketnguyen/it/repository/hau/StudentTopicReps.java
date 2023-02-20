package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.StudentTopic;
import com.hau.ketnguyen.it.model.request.hau.SearchStudentTopicRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentTopicReps extends CrudRepository<StudentTopic, Long> {
    List<StudentTopic> findByStudentIdInAndStatusApproveIsFalse(List<Long> studentIds);
    List<StudentTopic> findByStudentIdInAndStatusRegistryIsTrue(List<Long> studentIds);
    List<StudentTopic> findByStudentIdInAndStatusRegistryIsTrueAndStatusApproveIsTrue(List<Long> studentIds);
    List<StudentTopic> findByTopicIdInAndStatusRegistryIsTrueAndStatusApproveIsTrue(List<Long> topicIds);
    List<StudentTopic> findByStudentIdInAndStatusApproveIsTrue(List<Long> studentIds);
    List<StudentTopic> findByStudentIdInAndStatusSuggestIsTrue(List<Long> studentIds);
    List<StudentTopic> findByTopicIdIn(List<Long> topicIds);
    Optional<StudentTopic> findByStudentIdAndTopicId(Long studentId, Long topicId);
    List<StudentTopic> findByStudentId(Long studentId);
    Optional<StudentTopic> findByTopicIdAndStatusRegistryIsTrueAndStatusApproveIsTrue(Long topicId);

    @Query("SELECT st FROM student_topics st " +
            "LEFT JOIN topics t ON st.topicId = t.id " +
            "LEFT JOIN students s ON s.id = st.studentId " +
            "LEFT JOIN UserInfo ui ON s.userInfoId = ui.id " +
            "WHERE (st.topicId IN :#{#request.topicIds}) " +
            "AND (:#{#request.topicId} IS NULL OR st.topicId = :#{#request.topicId}) " +
            "AND (:#{#request.topicName} IS NULL OR lower(t.name) LIKE %:#{#request.topicName}%) " +
            "AND (:#{#request.studentName} IS NULL OR lower(ui.fullName) LIKE %:#{#request.studentName}%) " +
            "AND (:#{#request.studentId} IS NULL OR st.studentId = :#{#request.studentId})")
    Page<StudentTopic> search(SearchStudentTopicRequest request, Pageable pageable);

    @Query("select st from student_topics st")
    List<StudentTopic> findAll();
}
