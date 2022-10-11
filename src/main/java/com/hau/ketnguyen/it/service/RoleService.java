package com.hau.ketnguyen.it.service;

import com.hau.ketnguyen.it.entity.auth.Role;
import com.hau.ketnguyen.it.model.request.RoleRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;

public interface RoleService {
    PageDataResponse<Role> getAll(RoleRequest request);
}
