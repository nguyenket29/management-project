package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.Topics;
import com.hau.ketnguyen.it.model.request.hau.SearchStudentTopicRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchTopicRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchTopicStudentRequest;
import com.hau.ketnguyen.it.repository.projection.StatisticalProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicReps extends CrudRepository<Topics, Long> {
    List<Topics> findByIdIn(List<Long> ids);
    List<Topics> findByLecturerGuideIdIn(List<Long> lectureGuideIds);
    @Query("SELECT c FROM topics c WHERE " +
            " (COALESCE(:lectureIds, NULL) IS NULL OR c.lecturerCounterArgumentId IN :lectureIds) " +
            " AND (COALESCE(:lectureIds, NULL) IS NULL OR c.lecturerGuideId IN :lectureIds)")
    List<Topics> checkTopicWhenRemoveUser(List<Long> lectureIds);

    @Query("SELECT c FROM topics c " +
            "WHERE (:#{#request.lecturerGuideId} IS NULL OR c.lecturerGuideId = :#{#request.lecturerGuideId}) " +
            " AND (:#{#request.name} IS NULL OR lower(c.name) LIKE %:#{#request.name}%) " +
            " AND (c.statusSuggest IS TRUE) " +
            " AND (:#{#request.description} IS NULL OR lower(c.description) LIKE %:#{#request.description}%) " +
            " AND (:#{#request.status} IS NULL OR c.status = :#{#request.status}) " +
            " AND (:#{#request.statusSuggest} IS NULL OR c.statusSuggest = :#{#request.statusSuggest}) " +
            " AND (:#{#request.lecturerCounterArgumentId} IS NULL OR c.lecturerCounterArgumentId = :#{#request.lecturerCounterArgumentId}) " +
            " AND (:#{#request.year} IS NULL OR c.year = :#{#request.year}) " +
            " AND (:#{#request.categoryId} IS NULL OR c.categoryId = :#{#request.categoryId}) " +
            " ORDER BY c.id desc")
    Page<Topics> search(SearchTopicRequest request, Pageable pageable);
    List<Topics> findByStatus(boolean status);
    @Query("SELECT COUNT(t) FROM topics t WHERE (t.statusSuggest IS TRUE)")
    long getCount();

    @Query(value = "select t.name as nameTopic, ui.fullname as nameStudent, c.name as nameClass, t.year as topicYear, " +
            "t.score_assembly as scoreAssembly, t.score_guide as scoreGuide, t.score_counter_argument as scoreCounterArgument, " +
            "t.score_process_one as scoreProcessOne, t.score_process_two as scoreProcessTwo " +
            "FROM student_topics st " +
            "LEFT JOIN students s ON s.id = st.student_id " +
            "LEFT JOIN topics t  ON t.id = st.topic_id " +
            "LEFT JOIN classes c ON c.id = s.class_id " +
            "LEFT JOIN user_info ui ON ui.id = s.user_info_id " +
            "WHERE st.status is true", nativeQuery = true)
    List<StatisticalProjection> getStatistical(Pageable pageable);

    @Query(value = "select t.name as nameTopic, ui.fullname as nameStudent, c.name as nameClass, t.year as topicYear, " +
            "t.score_assembly as scoreAssembly, t.score_guide as scoreGuide, t.score_counter_argument as scoreCounterArgument, " +
            "t.score_process_one as scoreProcessOne, t.score_process_two as scoreProcessTwo " +
            "FROM student_topics st " +
            "LEFT JOIN students s ON s.id = st.student_id " +
            "LEFT JOIN topics t  ON t.id = st.topic_id " +
            "LEFT JOIN classes c ON c.id = s.class_id " +
            "LEFT JOIN user_info ui ON ui.id = s.user_info_id " +
            "WHERE st.status is true", nativeQuery = true)
    List<StatisticalProjection> getStatistical();

    @Query(value = "select count(*) from (select t.name as nameTopic, ui.fullname as nameStudent, c.name as nameClass, t.year as topicYear, " +
            "t.score_assembly as scoreAssembly, t.score_guide as scoreGuide, t.score_counter_argument as scoreCounterArgument, " +
            "t.score_process_one as scoreProcessOne, t.score_process_two as scoreProcessTwo " +
            "FROM student_topics st " +
            "LEFT JOIN students s ON s.id = st.student_id " +
            "LEFT JOIN topics t  ON t.id = st.topic_id " +
            "LEFT JOIN classes c ON c.id = s.class_id " +
            "LEFT JOIN user_info ui ON ui.id = s.user_info_id " +
            "WHERE st.status is true) as totals", nativeQuery = true)
    Long getStatisticalTotal();

    @Query("select t from topics t " +
            "where (t.id IN :#{#request.topicIds}) " +
            "and (:#{#request.topicName} IS NULL OR lower(t.name) LIKE %:#{#request.topicName}%) " +
            "and (:#{#request.lecturerGuideId} IS NULL OR t.lecturerGuideId = :#{#request.lecturerGuideId}) " +
            " AND (:#{#request.lecturerCounterArgumentId} IS NULL OR t.lecturerCounterArgumentId = :#{#request.lecturerCounterArgumentId}) " +
            " AND (:#{#request.year} IS NULL OR t.year = :#{#request.year}) " +
            " AND (:#{#request.description} IS NULL OR lower(t.description) LIKE %:#{#request.description}%) " +
            " AND (:#{#request.categoryId} IS NULL OR t.categoryId = :#{#request.categoryId})")
    Page<Topics> getListByTopicIds(SearchTopicStudentRequest request, Pageable pageable);

    @Query("select t from topics t where (t.statusSuggest is false)")
    List<Topics> getListByTopicIdSuggest();

    @Query("SELECT c FROM topics c " +
            "WHERE (:#{#request.lecturerGuideId} IS NULL OR c.lecturerGuideId = :#{#request.lecturerGuideId}) " +
            " AND (:#{#request.name} IS NULL OR lower(c.name) LIKE %:#{#request.name}%) " +
            " AND (c.statusSuggest IS FALSE ) " +
            " AND (:#{#request.description} IS NULL OR lower(c.description) LIKE %:#{#request.description}%) " +
            " AND (:#{#request.status} IS NULL OR c.status = :#{#request.status}) " +
            " AND (:#{#request.statusSuggest} IS NULL OR c.statusSuggest = :#{#request.statusSuggest}) " +
            " AND (:#{#request.lecturerCounterArgumentId} IS NULL OR c.lecturerCounterArgumentId = :#{#request.lecturerCounterArgumentId}) " +
            " AND (:#{#request.year} IS NULL OR c.year = :#{#request.year}) " +
            " AND (:#{#request.categoryId} IS NULL OR c.categoryId = :#{#request.categoryId}) " +
            " ORDER BY c.id desc")
    Page<Topics> searchTopicSuggest(SearchTopicRequest request, Pageable pageable);

    @Query("SELECT c FROM topics c WHERE lower(c.name) = :name")
    Optional<Topics> findByName(String name);
}
