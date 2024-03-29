package com.hau.ketnguyen.it.controller.auth;

import com.hau.ketnguyen.it.model.dto.auth.PasswordDTO;
import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import com.hau.ketnguyen.it.model.request.auth.SignupRequest;
import com.hau.ketnguyen.it.model.request.auth.TokenRefreshRequest;
import com.hau.ketnguyen.it.model.request.auth.UserInfoRequest;
import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.model.response.TokenRefreshResponse;
import com.hau.ketnguyen.it.model.response.UserResponse;
import com.hau.ketnguyen.it.service.AuthService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@Api(value = "Authentication Controller", description = "Các APIs quản lý xác thực")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/refresh-token")
    public ResponseEntity<APIResponse<TokenRefreshResponse>> tokenRefresh(@RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(APIResponse.success(authService.refreshToken(request)));
    }

    @PostMapping("/edit-user-info")
    public ResponseEntity<APIResponse<UserResponse>> editUserInfo(@RequestBody UserInfoRequest request) {
        return ResponseEntity.ok(APIResponse.success(authService.editUserInfo(request)));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<APIResponse<Void>> forgotPassword(@RequestBody PasswordDTO passwordDTO, HttpServletRequest request) throws MessagingException {
        authService.forgotPassword(passwordDTO.getEmail(), request);
        return ResponseEntity.ok(APIResponse.success());
    }

    @PostMapping("/update-password")
    public ResponseEntity<APIResponse<Boolean>> updatePassword(@RequestBody PasswordDTO passwordDTO) {
        return ResponseEntity.ok(APIResponse
                .success(authService.updatePassword(passwordDTO.getNewPassword(), passwordDTO.getToken(), passwordDTO.getOldPassword())));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<APIResponse<UserDTO>> signup(@RequestBody @Valid SignupRequest request, HttpServletRequest servletRequest) {
        return ResponseEntity.ok(APIResponse.success(authService.signup(request, servletRequest)));
    }

    @GetMapping("/activation")
    public ResponseEntity<APIResponse<Boolean>> verifyAccount(@RequestParam String code) {
        return ResponseEntity.ok(APIResponse.success(authService.verifyAccount(code)));
    }

//    @GetMapping("/forgot-password")
//    public ResponseEntity<APIResponse<Boolean>> forgotPassword(@RequestParam String email, HttpServletRequest request) throws MessagingException {
//        authService.forgotPassword(email, request);
//        return ResponseEntity.ok(APIResponse.success());
//    }

    @GetMapping("/account")
    public ResponseEntity<APIResponse<UserResponse>> getAccount() {
        return ResponseEntity.ok(APIResponse.success(authService.getInfo()));
    }

    @PostMapping("/log-out")
    public ResponseEntity<APIResponse<Void>> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.ok(APIResponse.success());
    }
}
