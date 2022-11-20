package com.hau.ketnguyen.it.controller.hau;

import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import com.hau.ketnguyen.it.model.request.auth.UserRequest;
import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController  {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<APIResponse<PageDataResponse<UserDTO>>> getAll(UserRequest request) {
        return ResponseEntity.ok(APIResponse.success(userService.getAll(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<UserDTO>> edit(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(APIResponse.success(userService.edit(id, userDTO)));
    }
}
