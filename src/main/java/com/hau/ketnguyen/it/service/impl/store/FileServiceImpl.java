package com.hau.ketnguyen.it.service.impl.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hau.ketnguyen.it.common.constant.Constants;
import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.FileUtil;
import com.hau.ketnguyen.it.entity.hau.Files;
import com.hau.ketnguyen.it.model.dto.hau.FileDTO;
import com.hau.ketnguyen.it.repository.hau.FileReps;
import com.hau.ketnguyen.it.repository.hau.TopicReps;
import com.hau.ketnguyen.it.service.FileService;
import com.hau.ketnguyen.it.service.mapper.FileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    private final TopicReps topicReps;
    private final FileMapper fileMapper;
    private final FileReps fileReps;
    private final Path root;

    public FileServiceImpl(TopicReps topicReps, FileMapper fileMapper, FileReps fileReps, @Value("${upload.path}") String root) {
        this.topicReps = topicReps;
        this.fileMapper = fileMapper;
        this.fileReps = fileReps;
        this.root = Paths.get(root);
    }

    @Override
    public FileDTO getFileById(Long id, HttpServletResponse response) throws Exception {
        FileDTO fileDTO = fileMapper.to(fileReps.findById(id).orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("File not exist !")));

        Path path = root.resolve(Paths.get(fileDTO.getPath())).normalize().toAbsolutePath();

        if (!java.nio.file.Files.exists(path)) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("File không tồn tại.");
        } else {
            fileDTO.setFileContent(FileUtil.readFile(path.toFile().getAbsolutePath()));
        }

        response.setContentType(fileDTO.getContentType());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDTO.getName() + "\"");
        return fileDTO;
    }

    @Override
    public FileDTO getFileByStringId(String id, HttpServletResponse response) throws Exception {
        Long fileId = Long.parseLong(id);
        FileDTO fileDTO = fileMapper.to(fileReps.findById(fileId).orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("File not exist !")));

        Path path = root.resolve(Paths.get(fileDTO.getPath())).normalize().toAbsolutePath();

        if (!java.nio.file.Files.exists(path)) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("File không tồn tại.");
        } else {
            fileDTO.setFileContent(FileUtil.readFile(path.toFile().getAbsolutePath()));
        }

        response.setContentType(fileDTO.getContentType());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDTO.getName() + "\"");
        return fileDTO;
    }

    @Override
    public List<Long> uploadFile(MultipartFile[] file) throws IOException {
        List<FileDTO> collect = new ArrayList<>();
        for (MultipartFile i : file) {
            collect.add(save(i));
        }
        return collect.stream().map(FileDTO::getId).collect(Collectors.toList());
    }

    @Override
    public FileDTO save(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            if (file.isEmpty()) {
                throw APIException.from(HttpStatus.NO_CONTENT).withMessage(file.getOriginalFilename() + "File trống.");
            }
            String fileName = String.valueOf(new Date().getTime()).concat("." + FileUtil.getImageExtension(file));
            Path filePath = Paths.get(fileName);
            String contentType = Objects.requireNonNull(file.getContentType()).isEmpty() ? "application/octet-stream" : file.getContentType();
            Files fileEntity = null;
            if (Constants.ImageExtension.imageExtensions.contains(FileUtil.getImageExtension(file))) {
                if (!java.nio.file.Files.exists(root)) {
                    java.nio.file.Files.createDirectories(root);
                }
                java.nio.file.Files.copy(inputStream, root.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                fileEntity = fileMapper.from(fileMapper.fileDTO(fileName, contentType, filePath.toString(), FileUtil.getImageExtension(file)));
            }
            return fileMapper.to(fileReps.save(fileEntity));
        }
    }
}
