package com.hau.ketnguyen.it.common.anotations;

import com.hau.ketnguyen.it.common.enums.RoleEnums;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AllowedRoles {
    RoleEnums.Role[] value();
}
