package com.hau.ketnguyen.it.repository.auth;

import com.hau.ketnguyen.it.entity.auth.User;
import com.hau.ketnguyen.it.model.request.auth.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserReps extends JpaRepository<User, Integer> {
    Optional<User> findByUsernameAndStatus(String username, short status);
    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    @Query("SELECT u FROM User u " +
            "WHERE (:#{#request.username} IS NULL OR u.username = :#{#request.username}) " +
            " AND (:#{#request.email} IS NULL OR u.email = :#{#request.email}) " +
            " AND (:#{#request.type} IS NULL OR u.type = :#{#request.type}) " +
            " AND (:#{#request.status} IS NULL OR u.status = :#{#request.status}) " +
            "ORDER BY u.id desc")
    Page<User> search(UserRequest request, Pageable pageable);
    @Query("SELECT u FROM User u WHERE (COALESCE(:ids, NULL) IS NULL OR u.id IN :ids)")
    List<User> findByIds(List<Integer> ids);
}
