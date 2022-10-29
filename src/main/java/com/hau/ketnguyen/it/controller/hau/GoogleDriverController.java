package com.hau.ketnguyen.it.controller.hau;

import com.hau.ketnguyen.it.model.dto.hau.FileDriverDTO;
import com.hau.ketnguyen.it.model.dto.hau.FolderDriverDTO;
import com.hau.ketnguyen.it.model.dto.hau.GoogleDriverFileDTO;
import com.hau.ketnguyen.it.model.dto.hau.GoogleDriverFolderDTO;
import com.hau.ketnguyen.it.model.response.APIResponse;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.GoogleDriverFile;
import com.hau.ketnguyen.it.service.GoogleDriverFolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/google-driver")
@AllArgsConstructor
@CrossOrigin
@Slf4j
public class GoogleDriverController {
    private final GoogleDriverFile googleDriveFileService;
    private final GoogleDriverFolder googleDriveFolderService;

    // Get all folder on drive
    @GetMapping("/folders")
    public ResponseEntity<APIResponse<PageDataResponse<GoogleDriverFolderDTO>>> getFolders() throws IOException, GeneralSecurityException {
        PageDataResponse<GoogleDriverFolderDTO> listFolder = googleDriveFolderService.getAllFolder();
        return ResponseEntity.ok(APIResponse.success(listFolder));
    }

    // Get all file on drive
    @GetMapping("/files")
    public ResponseEntity<APIResponse<PageDataResponse<GoogleDriverFileDTO>>> getFiles() throws IOException, GeneralSecurityException {
        PageDataResponse<GoogleDriverFileDTO> listFile = googleDriveFileService.getAllFile();
        return ResponseEntity.ok(APIResponse.success(listFile));
    }

    // Upload file to public
    @PostMapping(value = "/upload/file",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<APIResponse<Void>> uploadFile(@RequestParam("fileUpload") MultipartFile fileUpload,
                                                        @RequestParam("filePath") String pathFile,
                                                        @RequestParam("shared") String shared) {
        System.out.println(pathFile);
        if (pathFile.equals("")) {
            // Save to default folder if the user does not select a folder to save - you can change it
            pathFile = "Root";
        }
        googleDriveFileService.uploadFile(fileUpload, pathFile, Boolean.parseBoolean(shared));
        return ResponseEntity.ok(APIResponse.success());
    }

    // Delete file by id
    @DeleteMapping("/delete/file/{id}")
    public ResponseEntity<APIResponse<Void>> deleteFile(@PathVariable String id) throws Exception {
        googleDriveFileService.deleteFile(id);
        return ResponseEntity.ok(APIResponse.success());
    }

    // Download file
    @GetMapping("/download/file/{id}")
    public ResponseEntity<APIResponse<Void>> downloadFile(@PathVariable String id, HttpServletResponse response)
            throws IOException, GeneralSecurityException {
        googleDriveFileService.downloadFile(id, response.getOutputStream());
        return ResponseEntity.ok(APIResponse.success());
    }

    // Create folder
    @PostMapping("/create/folder")
    public ResponseEntity<APIResponse<Void>> createFolder(@RequestBody FolderDriverDTO folderDTO) throws Exception {
        googleDriveFolderService.createFolder(folderDTO.getFolderName());
        return ResponseEntity.ok(APIResponse.success());
    }

    // Delete folder by id
    @DeleteMapping("/delete/folder/{id}")
    public ResponseEntity<APIResponse<Void>> deleteFolder(@PathVariable String id) throws Exception {
        googleDriveFolderService.deleteFolder(id);
        return ResponseEntity.ok(APIResponse.success());
    }
}
