package com.hau.ketnguyen.it.service;

import com.hau.ketnguyen.it.model.dto.hau.GoogleDriverFolderDTO;
import com.hau.ketnguyen.it.model.response.PageDataResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface GoogleDriverFolder {
    PageDataResponse<GoogleDriverFolderDTO> getAllFolder() throws IOException, GeneralSecurityException;
    void createFolder(String folderName) throws Exception;
    void deleteFolder(String id) throws Exception;
}
