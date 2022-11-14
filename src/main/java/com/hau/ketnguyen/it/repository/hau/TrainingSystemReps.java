package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.TrainingSystem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingSystemReps extends CrudRepository<TrainingSystem, Long> {

}
