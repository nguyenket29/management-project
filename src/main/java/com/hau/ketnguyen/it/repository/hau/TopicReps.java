package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.Files;
import com.hau.ketnguyen.it.entity.hau.Topics;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicReps extends CrudRepository<Topics, Long> {
    List<Topics> findByIdIn(List<Long> ids);
}
