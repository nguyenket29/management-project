package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.StudentTopic;
import com.hau.ketnguyen.it.model.request.hau.SearchStudentTopicRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentTopicReps extends CrudRepository<StudentTopic, Long> {
    List<StudentTopic> findByStudentIdIn(List<Long> studentIds);
    List<StudentTopic> findByStudentIdInAndStatusIsTrue(List<Long> studentIds);
    List<StudentTopic> findByTopicIdIn(List<Long> topicIds);

    @Query("SELECT st FROM student_topics st " +
            "WHERE (COALESCE(:#{#request.topicIds}, NULL) IS NULL OR st.topicId IN :#{#request.topicIds}) " +
            "AND (:#{#request.topicId} IS NULL OR st.topicId = :#{#request.topicId}) " +
            "AND (:#{#request.studentId} IS NULL OR st.studentId = :#{#request.studentId})")
    Page<StudentTopic> search(SearchStudentTopicRequest request, Pageable pageable);
}
