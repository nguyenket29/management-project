package com.hau.ketnguyen.it.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImportExcelService {
    void importClassExcel(MultipartFile file);
    void importFacultyExcel(MultipartFile file);
    void importStudentExcel(MultipartFile file);
    void importLectureExcel(MultipartFile file);
    void importTopicExcel(MultipartFile file);
    void importTopicWorkplace(MultipartFile file);
    void importTopicCategory(MultipartFile file);
    void importTopicAssembly(MultipartFile file);
    void importExcelUser(MultipartFile file);
}
