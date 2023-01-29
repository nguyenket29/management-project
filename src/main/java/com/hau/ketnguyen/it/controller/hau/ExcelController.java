package com.hau.ketnguyen.it.controller.hau;

import com.hau.ketnguyen.it.model.request.auth.UserRequest;
import com.hau.ketnguyen.it.model.request.hau.*;
import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.service.ExcelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;

@RestController
@Api(value = "Excel Controller", description = "Các APIs excel")
@RequestMapping("/excels")
@AllArgsConstructor
@Slf4j
public class ExcelController {
    private final ExcelService excelService;

    @GetMapping("/category")
    @ApiOperation(value = "Export Category")
    public ResponseEntity<byte[]> exportExcelCategory(SearchCategoryRequest request, HttpServletResponse response)
            throws Exception {
        return ResponseEntity.ok(excelService.exportCategory(request, response));
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

    @GetMapping("/assembly")
    @ApiOperation(value = "Export Assembly")
    public ResponseEntity<APIResponse<Void>> exportExcelAssembly(SearchAssemblyRequest request, HttpServletResponse response)
            throws Exception {
        excelService.exportAssembly(request, response);
        return ResponseEntity.ok(APIResponse.success());
    }

    @GetMapping("/class")
    @ApiOperation(value = "Export Class")
    public ResponseEntity<APIResponse<Void>> exportExcelClass(SearchClassRequest request, HttpServletResponse response)
            throws Exception {
        excelService.exportClass(request, response);
        return ResponseEntity.ok(APIResponse.success());
    }

    @GetMapping("/lecture")
    @ApiOperation(value = "Export Lecture")
    public ResponseEntity<APIResponse<Void>> exportExcelLecture(SearchLecturerRequest request, HttpServletResponse response)
            throws Exception {
        excelService.exportLecture(request, response);
        return ResponseEntity.ok(APIResponse.success());
    }

    @GetMapping("/student")
    @ApiOperation(value = "Export User")
    public ResponseEntity<APIResponse<Void>> exportExcelStudent(SearchStudentRequest request, HttpServletResponse response)
            throws Exception {
        excelService.exportStudent(request, response);
        return ResponseEntity.ok(APIResponse.success());
    }

    @GetMapping("/topic")
    @ApiOperation(value = "Export Topic")
    public ResponseEntity<APIResponse<Void>> exportExcelTopic(SearchTopicRequest request, HttpServletResponse response)
            throws Exception {
        excelService.exportTopic(request, response);
        return ResponseEntity.ok(APIResponse.success());
    }
}
