package com.hau.ketnguyen.it.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImportExcelService {
    void importClassExcel(MultipartFile file);
    void importFacultyExcel(MultipartFile file);
    void importStudentExcel(MultipartFile file);
    void importLectureExcel(MultipartFile file);
}
