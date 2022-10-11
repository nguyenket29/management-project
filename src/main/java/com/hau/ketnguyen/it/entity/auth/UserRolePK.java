
package com.hau.ketnguyen.it.entity.auth;

import com.hau.ketnguyen.it.common.util.JsonUtil;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;


@Embeddable
public class UserRolePK implements Serializable {

    private static final long serialVersionUID = 932417267430880330L;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "role_id")
    private Integer roleId;

    public UserRolePK() {
        super();
    }

    public UserRolePK(Integer userId, Integer roleId) {
        super();
        this.userId = userId;
        this.roleId = roleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getroleId() {
        return roleId;
    }

    public void setroleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserRolePK other = (UserRolePK) obj;
        if (roleId == null) {
            if (other.roleId != null)
                return false;
        } else if (!roleId.equals(other.roleId))
            return false;
        if (userId == null) {
            return other.userId == null;
        } else return userId.equals(other.userId);
    }
}
