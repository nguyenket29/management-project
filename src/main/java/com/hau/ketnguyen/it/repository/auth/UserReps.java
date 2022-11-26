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
            " LEFT JOIN UserInfo ui " +
            " ON u.id = ui.userId " +
            "WHERE (:#{#request.username} IS NULL OR u.username = :#{#request.username}) " +
            " AND (:#{#request.email} IS NULL OR u.email = :#{#request.email}) " +
            " AND (:#{#request.fullName} IS NULL OR ui.fullName = :#{#request.fullName}) " +
            " AND (:#{#request.address} IS NULL OR ui.address = :#{#request.address}) " +
            " AND (:#{#request.phoneNumber} IS NULL OR ui.phoneNumber = :#{#request.phoneNumber}) " +
            " AND (:#{#request.town} IS NULL OR ui.town = :#{#request.town}) " +
            " AND (:#{#request.type} IS NULL OR u.type = :#{#request.type}) " +
            " AND (:#{#request.marriageStatus} IS NULL OR ui.marriageStatus = :#{#request.marriageStatus}) " +
            "ORDER BY u.id desc")
    Page<User> search(UserRequest request, Pageable pageable);
    @Query("SELECT u FROM User u WHERE (COALESCE(:ids, NULL) IS NULL OR u.id IN :ids)")
    List<User> findByIds(List<Integer> ids);
}
