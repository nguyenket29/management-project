package com.hau.ketnguyen.it.config.ggdriver;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class GoogleFileManager {
    private final GoogleDriverConfig googleDriverConfig;

    public GoogleFileManager(GoogleDriverConfig googleDriverConfig) {
        this.googleDriverConfig = googleDriverConfig;
    }

    // Get all file
    public List<File> listEverything() throws IOException, GeneralSecurityException {
        // Print the names and IDs for up to 10 files.
        FileList result = googleDriverConfig.getInstance().files().list()
                .setPageSize(1000)
                .setFields("nextPageToken, files(id, name, size, thumbnailLink, shared)") // get field of google drive file
                .execute();
        return result.getFiles();
    }

    //Get File By Id
    public File getFileById(String id) throws GeneralSecurityException, IOException {
        return googleDriverConfig.getInstance().files().get(id).execute();
    }


    // Get all folder
    public List<File> listFolderContent(String parentId) throws IOException, GeneralSecurityException {
        if (parentId == null) {
            parentId = "root";
        }
        String query = "'" + parentId + "' in parents";
        FileList result = googleDriverConfig.getInstance().files().list()
                .setQ(query)
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)") // get field of google drive folder
                .execute();
        return result.getFiles();
    }

    // Download file by id
    public void downloadFile(String id, OutputStream outputStream) throws IOException, GeneralSecurityException {
        if (id != null) {
            googleDriverConfig.getInstance().files()
                    .get(id).executeMediaAndDownloadTo(outputStream);
        }
    }

    // Delete file by id
    public void deleteFileOrFolder(String fileId) throws Exception {
        googleDriverConfig.getInstance().files().delete(fileId).execute();
    }

    // Set permission drive file
    private Permission setPermission(String type, String role) {
        Permission permission = new Permission();
        permission.setType(type);
        permission.setRole(role);
        return permission;
    }

    // Upload file
    public String uploadFile(MultipartFile file) {
        try {
            Drive driver = googleDriverConfig.getInstance();
            String folderId = "12W8UC1OQ2RETbvOxnPdSSmMRKabASKfO";
            if (null != file) {
                File fileMetadata = new File();
                fileMetadata.setParents(Collections.singletonList(folderId));
                fileMetadata.setName(file.getOriginalFilename());
                AbstractInputStreamContent fileContent = new
                        InputStreamContent(file.getContentType(), new ByteArrayInputStream(file.getBytes()));
                File uploadFile = driver.files().create(fileMetadata, fileContent).setFields("id").execute();
                return uploadFile.getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Upload Multi file
    public List<String> uploadMultiFile(MultipartFile[] files, String folderName, String type, String role) {
        try {
            List<String> fileIds = new ArrayList<>();
            if (files.length > 0) {
                Arrays.asList(files).forEach(file -> {
                    if (null != file) {
                        String fileId = uploadFile(file);
                        fileIds.add(fileId);
                    }
                });
            }
            return fileIds;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    // get id folder google drive
    public String getFolderId(String folderName) throws Exception {
        String parentId = null;
        String[] folderNames = folderName.split("/");

         Drive driveInstance = googleDriverConfig.getInstance();
        for (String name : folderNames) {
            parentId = findOrCreateFolder(parentId, name, driveInstance);
        }
        return parentId;
    }

    public String getFolderId(String folderName, Drive driveInstance) throws Exception {
        String parentId = null;
        String[] folderNames = folderName.split("/");
        for (String name : folderNames) {
            parentId = findOrCreateFolder(parentId, name, driveInstance);
        }
        return parentId;
    }

    private String findOrCreateFolder(String parentId, String folderName, Drive driveInstance) throws Exception {
        String folderId = searchFolderId(parentId, folderName, driveInstance);
        // Folder already exists, so return id
        if (folderId != null) {
            return folderId;
        }
        //Folder dont exists, create it and return folderId
        File fileMetadata = new File();
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        fileMetadata.setName(folderName);

        if (parentId != null) {
            fileMetadata.setParents(Collections.singletonList(parentId));
        }
        return driveInstance.files().create(fileMetadata)
                .setFields("id")
                .execute()
                .getId();
    }

    private String searchFolderId(String parentId, String folderName, Drive service) throws Exception {
        String folderId = null;
        String pageToken = null;
        FileList result = null;

        File fileMetadata = new File();
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        fileMetadata.setName(folderName);

        do {
            String query = " mimeType = 'application/vnd.google-apps.folder' ";
            if (parentId == null) {
                query = query + " and 'root' in parents";
            } else {
                query = query + " and '" + parentId + "' in parents";
            }
            result = service.files().list().setQ(query)
                    .setSpaces("drive")
                    .setFields("nextPageToken, files(id, name)")
                    .setPageToken(pageToken)
                    .execute();

            for (File file : result.getFiles()) {
                if (file.getName().equalsIgnoreCase(folderName)) {
                    folderId = file.getId();
                }
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null && folderId == null);

        return folderId;
    }
}
