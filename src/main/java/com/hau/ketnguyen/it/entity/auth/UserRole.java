package com.hau.ketnguyen.it.entity.auth;

import com.hau.ketnguyen.it.common.util.JsonUtil;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "user_role")
@Data
public class UserRole {

    @EmbeddedId
    private UserRolePK id = new UserRolePK();

    @Column(name = "user_id", insertable = false, updatable = false)
    private Integer userId;

    @Column(name = "role_id", insertable = false, updatable = false)
    private Integer roleId;

    public UserRole() {

    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
