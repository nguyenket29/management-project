package com.hau.ketnguyen.it.repository.auth;

import com.hau.ketnguyen.it.entity.auth.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleReps extends JpaRepository<Role, Integer> {
    Optional<Role> findByCode(String code);
    @Query("SELECT r FROM Role r")
    Page<Role> search(Pageable pageable);

    @Query("SELECT r FROM Role r WHERE (COALESCE(:codes, NULL) IS NULL OR r.code IN :codes)")
    List<Role> findByCodes(List<String> codes);
    List<Role> findByIdIn(List<Integer> roleIds);
}
