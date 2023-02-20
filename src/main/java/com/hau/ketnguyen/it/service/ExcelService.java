package com.hau.ketnguyen.it.service;

import com.hau.ketnguyen.it.model.request.auth.UserRequest;
import com.hau.ketnguyen.it.model.request.hau.*;

import javax.servlet.http.HttpServletResponse;

public interface ExcelService {
    byte[] exportCategory(SearchCategoryRequest request, HttpServletResponse response) throws Exception;
    void exportUser(UserRequest request, HttpServletResponse response) throws Exception;
    void exportFaculty(SearchFacultyRequest request, HttpServletResponse response) throws Exception;
    void exportWorkplace(SearchWorkplaceRequest request, HttpServletResponse response) throws Exception;
    void exportClass(SearchClassRequest request, HttpServletResponse response) throws Exception;
    void exportLecture(SearchLecturerRequest request, HttpServletResponse response) throws Exception;
    void exportAssembly(SearchAssemblyRequest request, HttpServletResponse response) throws Exception;
    void exportStudent(SearchStudentRequest request, HttpServletResponse response) throws Exception;
    void exportTopic(SearchTopicRequest request, HttpServletResponse response) throws Exception;
    void exportStatistical(HttpServletResponse response, SearchStatisticalRequest request) throws Exception;
}
