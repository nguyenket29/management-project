package com.hau.ketnguyen.it.service.impl.auth;

import com.google.api.services.drive.model.File;
import com.hau.ketnguyen.it.config.ggdriver.GoogleFileManager;
import com.hau.ketnguyen.it.convert.ConvertByteToMB;
import com.hau.ketnguyen.it.model.dto.hau.GoogleDriverFileDTO;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.GoogleDriverFile;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class GoogleDriverFileServiceImpl implements GoogleDriverFile {
    private final GoogleFileManager googleFileManager;

    @Override
    public PageDataResponse<GoogleDriverFileDTO> getAllFile() throws IOException, GeneralSecurityException {
        List<GoogleDriverFileDTO> responseList = null;
        List<File> files = googleFileManager.listEverything();
        GoogleDriverFileDTO dto = null;

        if (files != null) {
            responseList = new ArrayList<>();
            for (File f : files) {
                dto = new GoogleDriverFileDTO();
                if (f.getSize() != null) {
                    dto.setId(f.getId());
                    dto.setName(f.getName());
                    dto.setThumbnailLink(f.getThumbnailLink());
                    dto.setSize(ConvertByteToMB.getSize(f.getSize()));
                    dto.setLink("https://drive.google.com/file/d/" + f.getId() + "/view?usp=sharing");
                    dto.setShared(f.getShared());

                    responseList.add(dto);
                }
            }
        }
        return PageDataResponse.of(String.valueOf(responseList.size()), responseList);
    }

    @Override
    public void deleteFile(String id) throws Exception {
        googleFileManager.deleteFileOrFolder(id);
    }

    @Override
    public void uploadFile(MultipartFile file, String filePath, boolean isPublic) {
        String type = "";
        String role = "";
        if (isPublic) {
            // Public file of folder for everyone
            type = "anyone";
            role = "reader";
        } else {
            // Private
            type = "private";
            role = "private";
        }
        googleFileManager.uploadFile(file, filePath, type, role);
    }

    @Override
    public void downloadFile(String id, OutputStream outputStream) throws IOException, GeneralSecurityException {
        googleFileManager.downloadFile(id, outputStream);
    }

}
