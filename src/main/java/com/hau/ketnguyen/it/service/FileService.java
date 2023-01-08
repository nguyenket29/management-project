package com.hau.ketnguyen.it.service;

import com.hau.ketnguyen.it.model.dto.hau.FileDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface FileService {
    FileDTO getFileById(Long id, HttpServletResponse response) throws Exception;
    FileDTO getFileByStringId(String id, HttpServletResponse response) throws Exception;
    List<Long> uploadFile(MultipartFile[] file) throws IOException;
}
