package com.hau.ketnguyen.it.controller.hau;

import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.service.ImportExcelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(value = "Import Excel Controller", description = "Các APIs import excel")
@RequestMapping("/import-excel")
@AllArgsConstructor
@Slf4j
public class ImportExcelController {
    private final ImportExcelService importExcelService;

    @PostMapping("/class")
    @ApiOperation(value = "Import Class")
    public ResponseEntity<APIResponse<Void>> exportExcelClass(@RequestParam("fileUpload") MultipartFile fileUpload) throws Exception {
        importExcelService.importClassExcel(fileUpload);
        return ResponseEntity.ok(APIResponse.success());
    }

    @PostMapping("/faculty")
    @ApiOperation(value = "Import Faculty")
    public ResponseEntity<APIResponse<Void>> exportExcelFaculty(@RequestParam("fileUpload") MultipartFile fileUpload) throws Exception {
        importExcelService.importFacultyExcel(fileUpload);
        return ResponseEntity.ok(APIResponse.success());
    }

    @PostMapping("/student")
    @ApiOperation(value = "Import Student")
    public ResponseEntity<APIResponse<Void>> exportExcelStudent(@RequestParam("fileUpload") MultipartFile fileUpload) throws Exception {
        importExcelService.importStudentExcel(fileUpload);
        return ResponseEntity.ok(APIResponse.success());
    }

    @PostMapping("/lecture")
    @ApiOperation(value = "Import Lecture")
    public ResponseEntity<APIResponse<Void>> exportExcelLecture(@RequestParam("fileUpload") MultipartFile fileUpload) throws Exception {
        importExcelService.importLectureExcel(fileUpload);
        return ResponseEntity.ok(APIResponse.success());
    }

    @PostMapping("/topic")
    @ApiOperation(value = "Import Topic")
    public ResponseEntity<APIResponse<Void>> exportExcelTopic(@RequestParam("fileUpload") MultipartFile fileUpload) throws Exception {
        importExcelService.importTopicExcel(fileUpload);
        return ResponseEntity.ok(APIResponse.success());
    }

    @PostMapping("/workplace")
    @ApiOperation(value = "Import Workplace")
    public ResponseEntity<APIResponse<Void>> exportExcelWorkplace(@RequestParam("fileUpload") MultipartFile fileUpload) throws Exception {
        importExcelService.importTopicWorkplace(fileUpload);
        return ResponseEntity.ok(APIResponse.success());
    }

    @PostMapping("/category")
    @ApiOperation(value = "Import Category")
    public ResponseEntity<APIResponse<Void>> exportExcelCategory(@RequestParam("fileUpload") MultipartFile fileUpload) throws Exception {
        importExcelService.importTopicCategory(fileUpload);
        return ResponseEntity.ok(APIResponse.success());
    }

    @PostMapping("/assembly")
    @ApiOperation(value = "Import Assembly")
    public ResponseEntity<APIResponse<Void>> exportExcelAssembly(@RequestParam("fileUpload") MultipartFile fileUpload) throws Exception {
        importExcelService.importTopicAssembly(fileUpload);
        return ResponseEntity.ok(APIResponse.success());
    }
}
