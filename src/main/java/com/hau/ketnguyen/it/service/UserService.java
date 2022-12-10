package com.hau.ketnguyen.it.service;

import com.hau.ketnguyen.it.entity.auth.User;
import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import com.hau.ketnguyen.it.model.request.auth.UserRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import io.swagger.models.auth.In;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findByUsernameAndStatus(String username, short status);
    PageDataResponse<UserDTO> getAll(UserRequest request);
    UserDTO edit(Integer userId, UserRequest userRequest);
    void deleteUser(List<Integer> userIds);
    List<UserDTO> findById(List<Integer> userIds);
    void addRoleToUser(List<Integer> roleIds, List<Integer> userIds);
    void uploadAvatar(MultipartFile file, String filePath, boolean isPublic);
}
