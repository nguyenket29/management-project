package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.Topics;
import com.hau.ketnguyen.it.model.request.hau.SearchTopicRequest;
import com.hau.ketnguyen.it.repository.projection.StatisticalProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicReps extends CrudRepository<Topics, Long> {
    List<Topics> findByIdIn(List<Long> ids);
    @Query("SELECT c FROM topics c " +
            "WHERE (:#{#request.lecturerGuideId} IS NULL OR c.lecturerGuideId = :#{#request.lecturerGuideId}) " +
            " AND (:#{#request.name} IS NULL OR lower(c.name) LIKE %:#{#request.name}%) " +
            " AND (:#{#request.description} IS NULL OR lower(c.description) LIKE %:#{#request.description}%) " +
            " AND (:#{#request.status} IS NULL OR c.status = :#{#request.status}) " +
            " AND (:#{#request.lecturerCounterArgumentId} IS NULL OR c.lecturerCounterArgumentId = :#{#request.lecturerCounterArgumentId}) " +
            " AND (:#{#request.year} IS NULL OR c.year = :#{#request.year}) " +
            " ORDER BY c.id desc")
    Page<Topics> search(SearchTopicRequest request, Pageable pageable);
    List<Topics> findByStatus(boolean status);

    @Query(value = "select t.name as nameTopic, ui.fullname as nameStudent, c.name as nameClass, t.year as topicYear, " +
            "a.score as scoreAssembly, t.score_guide as scoreGuide, t.score_counter_argument as scoreCounterArgument " +
            "from students s, topics t, classes c, assembly a, user_info ui " +
            "where s.topic_id = t.id AND s.class_id = c.id AND t.id = a.topic_id AND ui.id = s.user_info_id", nativeQuery = true)
    List<StatisticalProjection> getStatistical(Pageable pageable);

    @Query(value = "select count(*) from (select t.name as nameTopic, ui.fullname as nameStudent, c.name as nameClass, t.year as topicYear, " +
            "a.score as scoreAssembly, t.score_guide as scoreGuide, t.score_counter_argument as scoreCounterArgument " +
            "from students s, topics t, classes c, assembly a, user_info ui " +
            "where s.topic_id = t.id AND s.class_id = c.id AND t.id = a.topic_id AND ui.id = s.user_info_id) as totals", nativeQuery = true)
    Long getStatisticalTotal();
}
