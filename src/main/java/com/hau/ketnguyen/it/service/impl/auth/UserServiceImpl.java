package com.hau.ketnguyen.it.service.impl.auth;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.PageableUtils;
import com.hau.ketnguyen.it.entity.auth.CustomUser;
import com.hau.ketnguyen.it.entity.auth.Role;
import com.hau.ketnguyen.it.entity.auth.User;
import com.hau.ketnguyen.it.entity.hau.Lecturers;
import com.hau.ketnguyen.it.entity.hau.Students;
import com.hau.ketnguyen.it.entity.hau.Topics;
import com.hau.ketnguyen.it.entity.hau.UserInfo;
import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import com.hau.ketnguyen.it.model.request.auth.SignupRequest;
import com.hau.ketnguyen.it.model.request.auth.UserRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.repository.auth.RoleReps;
import com.hau.ketnguyen.it.repository.auth.UserInfoReps;
import com.hau.ketnguyen.it.repository.auth.UserReps;
import com.hau.ketnguyen.it.repository.hau.LecturerReps;
import com.hau.ketnguyen.it.repository.hau.StudentReps;
import com.hau.ketnguyen.it.repository.hau.TopicReps;
import com.hau.ketnguyen.it.service.FileService;
import com.hau.ketnguyen.it.service.GoogleDriverFile;
import com.hau.ketnguyen.it.service.UserService;
import com.hau.ketnguyen.it.service.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.hau.ketnguyen.it.common.enums.TypeUser.*;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserReps userReps;
    private final RoleReps roleReps;
    private final UserInfoReps userInfoReps;
    private final FileService fileService;
    private final StudentReps studentReps;
    private final LecturerReps lecturerReps;
    private final GoogleDriverFile googleDriverFile;
    private final TopicReps topicReps;

    @Override
    @Transactional
    public boolean createUser(SignupRequest signupRequest) {
        User user = new User();

        validRegister(signupRequest);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (signupRequest.getType() != null) {
            if (signupRequest.getType().equalsIgnoreCase(STUDENT.name())) {
                user.setType(STUDENT.name());
            } else if (signupRequest.getType().equalsIgnoreCase(LECTURE.name())) {
                user.setType(LECTURE.name());
                if (signupRequest.getStudentOrLectureId() != null) {

                } else {
                    throw APIException.from(HttpStatus.BAD_REQUEST).withMessage("Vui lòng chọn giảng viên để tạo tài khoản!");
                }
            } else {
                user.setType(OTHER.name());
            }
        } else {
            throw APIException.from(HttpStatus.BAD_REQUEST).withMessage("Vui lòng chọn tài khoản!");
        }

        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setStatus(User.Status.ACTIVE);
        User userEntity = userReps.save(user);

        if (userEntity.getId() > 0) {
            if (signupRequest.getType().equalsIgnoreCase(STUDENT.name())) {
                if (signupRequest.getStudentOrLectureId() != null) {
                    Optional<Students> studentOptional = studentReps.findById(signupRequest.getStudentOrLectureId());

                    if (studentOptional.isEmpty()) {
                        throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy sinh viên");
                    }

                    studentOptional.get().setUserId(userEntity.getId());
                    studentReps.save(studentOptional.get());
                } else {
                    throw APIException.from(HttpStatus.BAD_REQUEST).withMessage("Vui lòng chọn sinh viên để tạo tài khoản!");
                }
            } else if (signupRequest.getType().equalsIgnoreCase(LECTURE.name())) {
                Optional<Lecturers> lecturersOptional = lecturerReps.findById(signupRequest.getStudentOrLectureId());

                if (lecturersOptional.isEmpty()) {
                    throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy giảng viên");
                }

                lecturersOptional.get().setUserId(userEntity.getId());
                lecturerReps.save(lecturersOptional.get());
            }
            return true;
        }

        return false;
    }

    private void validRegister(SignupRequest signupRequest) {
        if (userReps.existsByUsername(signupRequest.getUsername())) {
            throw APIException.from(HttpStatus.BAD_REQUEST).withMessage("Username is existed.");
        }

        if (userReps.existsByEmail(signupRequest.getEmail())) {
            throw APIException.from(HttpStatus.BAD_REQUEST).withMessage("Email is existed.");
        }

        if (signupRequest.getPassword() == null || signupRequest.getConfirmPassword() == null
                || !signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            throw APIException.from(HttpStatus.BAD_REQUEST).withMessage("Tài khoản hoặc mật khẩu không chnh xác!");
        }
    }

    /**
     * Tìm kiếm người dùng theo username và status
     *
     * @Param: username
     * @Param: status
     * @Return: User
     */
    @Override
    public Optional<User> findByUsernameAndStatus(String username, short status) {
        log.info("calling findByUsernameAndStatus");
        return userReps.findByUsernameAndStatus(username, status);
    }

    /**
     * Danh sách người dùng
     *
     * @Param: request
     * @Return: PageDataResponse<UserDTO>
     */
    @Override
    public PageDataResponse<UserDTO> getAll(UserRequest request) {
        if (request.getUsername() != null) {
            request.setUsername(request.getUsername().toLowerCase());
        }

        if (request.getEmail() != null) {
            request.setEmail(request.getEmail().toLowerCase());
        }

        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        Page<UserDTO> pageData = userReps.search(request, pageable).map(userMapper::to);
        return PageDataResponse.of(pageData);
    }

    /**
     * Chỉnh sửa người dùng
     *
     * @Param: userId
     * @RequestBody: UserDTO
     * @Return: UserDTO
     */
    @Override
    @Transactional
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

        return userMapper.to(userReps.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(List<Integer> userIds) {
        if (userIds != null && !userIds.isEmpty()) {
            List<User> users = userReps.findByIds(userIds);

            List<Students> students = studentReps.findByUserIdIn(userIds);
            List<Lecturers> lecturers = lecturerReps.findByUserIdIn(userIds);
            checkStudent(students);

            if (!users.isEmpty()) {
                List<Integer> idUsers = new ArrayList<>();
                users.forEach(u -> {
                    if (OTHER.name().equalsIgnoreCase(u.getType())) {
                        idUsers.add(u.getId());
                    }
                });

                if (!CollectionUtils.isEmpty(idUsers)) {
                    userInfoReps.deleteAllByUserIdIn(idUsers);
                }
                userReps.deleteAll(users);
            }

            if (!CollectionUtils.isEmpty(students)) {
                students.forEach(s -> s.setUserId(null));
                studentReps.saveAll(students);
            }

            if (!CollectionUtils.isEmpty(lecturers)) {
                lecturers.forEach(s -> s.setUserId(null));
                lecturerReps.saveAll(lecturers);
            }
        }
    }

    private void checkStudent(List<Students> students) {
        if (!CollectionUtils.isEmpty(students)) {
            students.forEach(s -> {
                if (s.getTopicId() != null) {
                    throw APIException.from(HttpStatus.BAD_REQUEST)
                            .withMessage("Sinh viên hoặc giáo viên đang thực hiện hoặc hướng dẫn đồ án, không thể xóa!");
                }
            });
        }
    }

    @Override
    public List<UserDTO> findById(List<Integer> userIds) {
        if (userIds != null && !userIds.isEmpty()) {
            return userReps.findByIds(userIds)
                    .stream().map(userMapper::to).collect(Collectors.toList());
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
    public String uploadAvatar(MultipartFile file) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser user = (CustomUser) authentication.getPrincipal();
        String fileId = fileService.save(file).getId().toString();
        UserInfo userInfo = new UserInfo();
        if (user.getType().equalsIgnoreCase(STUDENT.name())) {
            Optional<Students> studentOptional = studentReps.findByUserId(user.getId());

            if (studentOptional.isEmpty()) {
                throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy sinh viên");
            }

            userInfo = userInfoReps.findById(studentOptional.get().getUserInfoId())
                    .orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy thông tin sinh viên"));
        } else if (user.getType().equalsIgnoreCase(LECTURE.name())) {
            Optional<Lecturers> lecturersOptional = lecturerReps.findByUserId(user.getId());

            if (lecturersOptional.isEmpty()) {
                throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy giảng viên");
            }

            userInfo = userInfoReps.findById(lecturersOptional.get().getUserInfoId())
                    .orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy thông tin giảng viên"));
        } else {
            userInfo = userInfoReps.findByUserId(user.getId())
                    .orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy thông tin tài khoản"));
        }

        userInfo.setAvatar(fileId);
        userInfoReps.save(userInfo);

        return fileId;
    }

    @Override
    @Transactional
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
