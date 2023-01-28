package com.hau.ketnguyen.it.controller.hau;

import com.hau.ketnguyen.it.model.request.auth.UserRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchCategoryRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchFacultyRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchWorkplaceRequest;
import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.service.ExcelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Api(value = "Excel Controller", description = "Các APIs excel")
@RequestMapping("/excels")
@AllArgsConstructor
@Slf4j
public class ExcelController {
    private final ExcelService excelService;

    @GetMapping("/category")
    @ApiOperation(value = "Export Category")
    public ResponseEntity<APIResponse<Void>> exportExcelCategory(SearchCategoryRequest request, HttpServletResponse response)
            throws Exception {
        excelService.exportCategory(request, response);
        return ResponseEntity.ok(APIResponse.success());
    }

    @GetMapping("/user")
    @ApiOperation(value = "Export User")
    public ResponseEntity<APIResponse<Void>> exportExcelUser(UserRequest request, HttpServletResponse response)
            throws Exception {
        excelService.exportUser(request, response);
        return ResponseEntity.ok(APIResponse.success());
    }

    @GetMapping("/faculty")
    @ApiOperation(value = "Export User")
    public ResponseEntity<APIResponse<Void>> exportExcelFaculty(SearchFacultyRequest request, HttpServletResponse response)
            throws Exception {
        excelService.exportFaculty(request, response);
        return ResponseEntity.ok(APIResponse.success());
    }

    @GetMapping("/workplace")
    @ApiOperation(value = "Export User")
    public ResponseEntity<APIResponse<Void>> exportExcelWorkplace(SearchWorkplaceRequest request, HttpServletResponse response)
            throws Exception {
        excelService.exportWorkplace(request, response);
        return ResponseEntity.ok(APIResponse.success());
    }
}
