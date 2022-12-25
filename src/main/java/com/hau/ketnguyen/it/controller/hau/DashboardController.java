package com.hau.ketnguyen.it.controller.hau;

import com.hau.ketnguyen.it.model.dto.hau.DashboardDTO;
import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.service.DashboardService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Dashboard Controller", description = "Các APIs dashboard")
@RequestMapping("/dashboards")
@AllArgsConstructor
@Slf4j
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<APIResponse<DashboardDTO>> dashboard() {
        return ResponseEntity.ok(APIResponse.success(dashboardService.dashboard()));
    }

}
