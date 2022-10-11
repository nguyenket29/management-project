package com.hau.ketnguyen.it.controller;

import com.hau.ketnguyen.it.model.response.APIResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@AllArgsConstructor
public class HomeController {
    @GetMapping("/")
    public ResponseEntity<APIResponse<String>> home() {
        return ResponseEntity.ok(APIResponse.success("Hello"));
    }
}
