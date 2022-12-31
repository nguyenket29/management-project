package com.hau.ketnguyen.it.repository.auth;

import com.hau.ketnguyen.it.entity.hau.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfoReps extends JpaRepository<UserInfo, Long> {
    List<UserInfo> findByIdIn(List<Long> userIds);
    @Query("select ui from UserInfo ui")
    List<UserInfo> getListUserInfo();
}
