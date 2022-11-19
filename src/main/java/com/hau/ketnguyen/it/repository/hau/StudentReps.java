package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.Students;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentReps extends CrudRepository<Students, Long> {
    List<Students> findByClassIdIn(List<Long> classIds);
}
