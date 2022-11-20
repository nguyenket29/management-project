package com.hau.ketnguyen.it.service.impl.store;

import com.google.api.services.drive.model.File;
import com.hau.ketnguyen.it.config.ggdriver.GoogleFileManager;
import com.hau.ketnguyen.it.model.dto.hau.GoogleDriverFolderDTO;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.service.GoogleDriverFolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class GoogleDriverFolderServiceImpl implements GoogleDriverFolder {
    private final GoogleFileManager googleFileManager;

    @Override
    public PageDataResponse<GoogleDriverFolderDTO> getAllFolder() throws IOException, GeneralSecurityException {
        List<File> files = googleFileManager.listFolderContent("root");

        List<GoogleDriverFolderDTO> responseList = null;
        GoogleDriverFolderDTO dto = null;

        if (files != null) {
            responseList = new ArrayList<>();
            for (File f : files) {
                dto = new GoogleDriverFolderDTO();
                dto.setId(f.getId());
                dto.setName(f.getName());
                dto.setLink("https://drive.google.com/drive/u/3/folders/"+f.getId());
                responseList.add(dto);
            }
        }
        return PageDataResponse.of(String.valueOf(responseList.size()), responseList);
    }

    @Override
    public void createFolder(String folderName) throws Exception {
        String folderId = googleFileManager.getFolderId(folderName);
        System.out.println(folderId);
    }

    @Override
    public void deleteFolder(String id) throws Exception {
        googleFileManager.deleteFileOrFolder(id);
    }
}
