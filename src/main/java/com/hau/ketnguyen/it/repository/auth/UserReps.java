package com.hau.ketnguyen.it.repository.auth;

import com.hau.ketnguyen.it.entity.auth.User;
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
    @Query("SELECT u FROM User u")
    Page<User> search(Pageable pageable);

    /*@Query(value = "SELECT " +
            "distinct r.code " +
            "FROM " +
            "   user_group ug, " +
            "   users u, " +
            "   group_role gr, " +
            "   role r " +
            "WHERE " +
            "   (:username is null or u.username = :username) " +
            "   AND (:status is null or  u.status = :status) " +
            "   AND u.id = ug.user_id " +
            "   and ug.group_id = gr.group_id " +
            "   and gr.role_id = r.id", nativeQuery = true)
    Set<String> getRolesByUsername(String username, Short status);*/

    @Query("SELECT u FROM User u WHERE (COALESCE(:ids, NULL) IS NULL OR u.id IN :ids)")
    List<User> findByIds(List<Integer> ids);
}
