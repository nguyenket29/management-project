package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.Faculties;
import com.hau.ketnguyen.it.entity.hau.Files;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyReps extends CrudRepository<Faculties, Long> {

}
