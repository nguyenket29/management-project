package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.Workplaces;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkplaceReps extends CrudRepository<Workplaces, Long> {

}
