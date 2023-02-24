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
    public ResponseEntity<APIResponse<Void>> exportExcelClass(@RequestParam("file") MultipartFile file) throws Exception {
        importExcelService.importClassExcel(file);
        return ResponseEntity.ok(APIResponse.success());
    }

    @PostMapping("/faculty")
    @ApiOperation(value = "Import Faculty")
    public ResponseEntity<APIResponse<Void>> exportExcelFaculty(@RequestParam("file") MultipartFile file) throws Exception {
        importExcelService.importFacultyExcel(file);
        return ResponseEntity.ok(APIResponse.success());
    }

    @PostMapping("/student")
    @ApiOperation(value = "Import Student")
    public ResponseEntity<APIResponse<Void>> exportExcelStudent(@RequestParam("file") MultipartFile file) throws Exception {
        importExcelService.importStudentExcel(file);
        return ResponseEntity.ok(APIResponse.success());
    }

    @PostMapping("/lecture")
    @ApiOperation(value = "Import Lecture")
    public ResponseEntity<APIResponse<Void>> exportExcelLecture(@RequestParam("file") MultipartFile file) throws Exception {
        importExcelService.importLectureExcel(file);
        return ResponseEntity.ok(APIResponse.success());
    }

    @PostMapping("/topic")
    @ApiOperation(value = "Import Topic")
    public ResponseEntity<APIResponse<Void>> exportExcelTopic(@RequestParam("file") MultipartFile file) throws Exception {
        importExcelService.importTopicExcel(file);
        return ResponseEntity.ok(APIResponse.success());
    }

    @PostMapping("/workplace")
    @ApiOperation(value = "Import Workplace")
    public ResponseEntity<APIResponse<Void>> exportExcelWorkplace(@RequestParam("file") MultipartFile file) throws Exception {
        importExcelService.importTopicWorkplace(file);
        return ResponseEntity.ok(APIResponse.success());
    }

    @PostMapping("/category")
    @ApiOperation(value = "Import Category")
    public ResponseEntity<APIResponse<Void>> exportExcelCategory(@RequestParam("file") MultipartFile file) throws Exception {
        importExcelService.importTopicCategory(file);
        return ResponseEntity.ok(APIResponse.success());
    }

    @PostMapping("/assembly")
    @ApiOperation(value = "Import Assembly")
    public ResponseEntity<APIResponse<Void>> exportExcelAssembly(@RequestParam("file") MultipartFile file) throws Exception {
        importExcelService.importTopicAssembly(file);
        return ResponseEntity.ok(APIResponse.success());
    }
}
