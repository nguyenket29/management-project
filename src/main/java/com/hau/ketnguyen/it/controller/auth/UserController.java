package com.hau.ketnguyen.it.controller.auth;

import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import com.hau.ketnguyen.it.model.dto.hau.AssemblyDTO;
import com.hau.ketnguyen.it.model.request.auth.SignupRequest;
import com.hau.ketnguyen.it.model.request.auth.UserRequest;
import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Api(value = "User Controller", description = "Các APIs quản lý người dùng")
@RequestMapping("/users")
@AllArgsConstructor
public class UserController  {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<APIResponse<PageDataResponse<UserDTO>>> getAll(UserRequest request) {
        return ResponseEntity.ok(APIResponse.success(userService.getAll(request)));
    }

    @PostMapping
    public ResponseEntity<APIResponse<Boolean>> create(SignupRequest request) {
        return ResponseEntity.ok(APIResponse.success(userService.createUser(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<UserDTO>> edit(@PathVariable Integer id, @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(APIResponse.success(userService.edit(id, userRequest)));
    }

    @DeleteMapping
    public ResponseEntity<APIResponse<Void>> delete(List<Integer> ids) {
        userService.deleteUser(ids);
        return ResponseEntity.ok(APIResponse.success());
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<APIResponse<UserDTO>> findById(@RequestParam List<Integer> ids) {
        return ResponseEntity.ok(APIResponse.success(userService.findById(ids).get(0)));
    }

    @GetMapping("/inactive")
    public ResponseEntity<APIResponse<Void>> inActiveAcc(@RequestParam Integer userId, @RequestParam boolean check) {
        userService.inActive(userId, check);
        return ResponseEntity.ok(APIResponse.success());
    }

    @PostMapping("/upload-avatar")
    public ResponseEntity<APIResponse<Void>> uploadAvatar(@RequestParam("fileUpload") MultipartFile fileUpload,
                                                             @RequestParam("filePath") String pathFile,
                                                             @RequestParam("shared") String shared) {
        userService.uploadAvatar(fileUpload, pathFile, Boolean.parseBoolean(shared));
        return ResponseEntity.ok(APIResponse.success());
    }
}
