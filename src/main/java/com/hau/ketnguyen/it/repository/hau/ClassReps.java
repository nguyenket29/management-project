package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.Classes;
import com.hau.ketnguyen.it.entity.hau.Files;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassReps extends CrudRepository<Classes, Long> {

}
