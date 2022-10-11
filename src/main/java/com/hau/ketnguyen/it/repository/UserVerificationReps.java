package com.hau.ketnguyen.it.repository;

import com.hau.ketnguyen.it.entity.auth.UserVerification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserVerificationReps extends CrudRepository<UserVerification, Long> {
    Optional<UserVerification> findByCode(String code);
    Optional<UserVerification> findByUserId(Integer userId);
}
