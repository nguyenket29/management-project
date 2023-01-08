package com.hau.ketnguyen.it.controller.hau;

import com.hau.ketnguyen.it.model.request.hau.FileRequest;
import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Api(value = "FileController", description = "Upload file, save path file, get file from path")
@RestController
@AllArgsConstructor
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    @ApiOperation("Upload file")
    @PostMapping(value = "/upload")
    public ResponseEntity<APIResponse<List<Long>>> upload(@RequestParam("file") MultipartFile[] file)
            throws IOException {
        return ResponseEntity.ok()
                .body(APIResponse.success(fileService.uploadFile(file), "Upload file successfully !"));
    }

    @ApiOperation("Download file")
    @GetMapping(value = "/download/{id}")
    public ResponseEntity<byte[]> downFile(@PathVariable("id") Long id, HttpServletResponse response) throws Exception {
        return ResponseEntity.ok()
                .body(fileService.getFileById(id, response).getFileContent());
    }

    @ApiOperation("Download file string id")
    @GetMapping(value = "/download/string/{id}")
    public ResponseEntity<byte[]> downFileStringId(@PathVariable("id") String id, HttpServletResponse response) throws Exception {
        return ResponseEntity.ok()
                .body(fileService.getFileByStringId(id, response).getFileContent());
    }
}
