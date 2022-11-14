package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.hau.Files;
import com.hau.ketnguyen.it.entity.hau.Lecturers;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LecturerReps extends CrudRepository<Lecturers, Long> {

}
