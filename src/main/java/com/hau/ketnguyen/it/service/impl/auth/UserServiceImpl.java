package com.hau.ketnguyen.it.service.impl.auth;

import com.hau.ketnguyen.it.common.enums.TypeUser;
import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.PageableUtils;
import com.hau.ketnguyen.it.entity.auth.CustomUser;
import com.hau.ketnguyen.it.entity.auth.Role;
import com.hau.ketnguyen.it.entity.auth.User;
import com.hau.ketnguyen.it.entity.hau.UserInfo;
import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import com.hau.ketnguyen.it.model.dto.auth.UserInfoDTO;
import com.hau.ketnguyen.it.model.request.auth.UserRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.repository.auth.RoleReps;
import com.hau.ketnguyen.it.repository.auth.UserInfoReps;
import com.hau.ketnguyen.it.repository.auth.UserReps;
import com.hau.ketnguyen.it.service.GoogleDriverFile;
import com.hau.ketnguyen.it.service.UserService;
import com.hau.ketnguyen.it.service.mapper.UserInfoMapper;
import com.hau.ketnguyen.it.service.mapper.UserMapper;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserReps userReps;
    private final RoleReps roleReps;
    private final UserInfoReps userInfoReps;
    private final UserInfoMapper userInfoMapper;
    private final GoogleDriverFile googleDriverFile;

    /**
     * Tìm kiếm người dùng theo username và status
     *
     * @Param: username
     * @Param: status
     * @Return: User
     * */
    @Override
    @Cacheable("users")
    public Optional<User> findByUsernameAndStatus(String username, short status) {
        log.info("calling findByUsernameAndStatus");
        return userReps.findByUsernameAndStatus(username, status);
    }

    /**
     * Danh sách người dùng
     *
     * @Param: request
     * @Return: PageDataResponse<UserDTO>
     *
     * */
    @Override
    public PageDataResponse<UserDTO> getAll(UserRequest request) {
        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        Map<Integer, UserInfoDTO> mapUserInfo = userInfoReps.getListUserInfo()
                .stream().map(userInfoMapper::to).collect(Collectors.toMap(UserInfoDTO::getUserId, ui -> ui));
        Page<UserDTO> pageData = userReps.search(request, pageable).map(userMapper::to);

        if (!pageData.isEmpty()) {
            pageData.forEach(p -> {
                if (mapUserInfo.containsKey(p.getId())) {
                    p.setUserInfoDTO(mapUserInfo.get(p.getId()));
                }
            });
        }

        return PageDataResponse.of(pageData);
    }

    /**
     * Chỉnh sửa người dùng
     *
     * @Param: userId
     * @RequestBody: UserDTO
     * @Return: UserDTO
     * */
    @Override
    public UserDTO edit(Integer userId, UserRequest userRequest) {
        Optional<User> userOptional = userReps.findById(userId);

        if (userOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không thể tìm thấy người dùng với id  " + userOptional);
        }

        // update account and roles to user
        User user = userOptional.get();
        user.setEmail(userRequest.getEmail());
        user.setStatus(userRequest.getStatus());
        user.setUsername(userRequest.getUsername());

        if (userRequest.getListRole() != null) {
            List<Role> roles = roleReps.findByCodes(userRequest.getListRole());
            Set<Role> roleSet = new HashSet<>(roles);
            user.setRoles(roleSet);
        }

        // update user info
        UserInfo userInfo = userInfoReps.findByUserId(user.getId())
                .orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Username not found."));
        userInfo.setGender(userRequest.getGender());
        userInfo.setAddress(userRequest.getAddress());
        userInfo.setAvatar(userRequest.getAvatar());
        userInfo.setTown(userRequest.getTown());
        userInfo.setDateOfBirth(userRequest.getDateOfBirth());
        userInfo.setFullName(userRequest.getFullName());
        userInfo.setMarriageStatus(userRequest.getMarriageStatus());
        userInfo.setPhoneNumber(userRequest.getPhoneNumber());

        UserDTO userDTO = userMapper.to(userReps.save(user));
        UserInfoDTO userInfoDTO = userInfoMapper.to(userInfoReps.save(userInfo));

        if (Objects.equals(userInfoDTO.getUserId(), userDTO.getId())) {
            userDTO.setUserInfoDTO(userInfoDTO);
        }

        return userDTO;
    }

    @Override
    public void deleteUser(List<Integer> userIds) {
        if (userIds != null && !userIds.isEmpty()) {
            List<User> users = userReps.findByIds(userIds);
            List<UserInfo> userInfos = userInfoReps.findByUserIdIn(userIds);
            if (!users.isEmpty() && !userInfos.isEmpty()) {
                userReps.deleteAll(users);
                userInfoReps.deleteAll(userInfos);
            }
        }
    }

    @Override
    public List<UserDTO> findById(List<Integer> userIds) {
        if (userIds != null && !userIds.isEmpty()) {
            List<UserDTO> users = userReps.findByIds(userIds)
                    .stream().map(userMapper::to).collect(Collectors.toList());
            Map<Integer, UserInfoDTO> userInfos = userInfoReps.findByUserIdIn(userIds)
                    .stream().map(userInfoMapper::to).collect(Collectors.toMap(UserInfoDTO::getUserId, u -> u));

            if (!users.isEmpty()) {
                users.forEach(u -> {
                    if (!userInfos.isEmpty() && userInfos.containsKey(u.getId())) {
                        u.setUserInfoDTO(userInfos.get(u.getId()));
                    }
                });
            }

            return users;
        }
        return null;
    }

    @Override
    public void addRoleToUser(List<Integer> roleIds, List<Integer> userIds) {
        if (roleIds != null && !roleIds.isEmpty()) {
            Set<Role> roles = new HashSet<>(roleReps.findByIdIn(roleIds));
            if (userIds != null && !userIds.isEmpty()) {
                List<User> users = userReps.findByIds(userIds);

                if (!users.isEmpty() && !roleIds.isEmpty()) {
                    users.forEach(u -> u.setRoles(roles));
                    userReps.saveAll(users);
                }
            }
        }
    }

    @Override
    public void uploadAvatar(MultipartFile file, String filePath, boolean isPublic) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser user = (CustomUser) authentication.getPrincipal();
        String fileId = googleDriverFile.uploadFile(file, filePath, isPublic);
        if (fileId != null) {
            Optional<UserInfo> userInfoOptional = userInfoReps.findByUserId(user.getId());

            if (userInfoOptional.isEmpty()) {
                throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy người dùng");
            }

            UserInfo userInfo = userInfoOptional.get();
            userInfo.setAvatar(fileId);

            userInfoReps.save(userInfo);
        }
    }

    @Override
    public void inActive(Integer userId, boolean check) {
        Optional<User> userOptional = userReps.findById(userId);

        if (userOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không thể tìm thấy người dùng với id  " + userOptional);
        }

        if (check) {
            userOptional.get().setStatus(User.Status.ACTIVE);
        } else {
            userOptional.get().setStatus(User.Status.LOCK);
        }

        userReps.save(userOptional.get());
    }
}
