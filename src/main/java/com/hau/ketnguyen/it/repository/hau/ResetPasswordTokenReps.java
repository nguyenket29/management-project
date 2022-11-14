package com.hau.ketnguyen.it.repository.hau;

import com.hau.ketnguyen.it.entity.auth.ResetPasswordToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordTokenReps extends CrudRepository<ResetPasswordToken, Long> {
    Optional<ResetPasswordToken> findByToken(String token);
}
