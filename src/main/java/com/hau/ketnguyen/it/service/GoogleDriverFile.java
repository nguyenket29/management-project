package com.hau.ketnguyen.it.service;

import com.hau.ketnguyen.it.model.dto.hau.GoogleDriverFileDTO;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.List;

public interface GoogleDriverFile {
    PageDataResponse<GoogleDriverFileDTO> getAllFile() throws IOException, GeneralSecurityException;
    void deleteFile(String id) throws Exception;
    String uploadFile(MultipartFile file, String filePath, boolean isPublic);
    List<String> uploadMultiFile(MultipartFile[] file, String filePath, boolean isPublic);
    byte[] downloadFile(String id, OutputStream outputStream, HttpServletResponse response) throws IOException, GeneralSecurityException;
    List<GoogleDriverFileDTO> findByIdFiles(List<String> fileId) throws GeneralSecurityException, IOException;
}
