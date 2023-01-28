package com.hau.ketnguyen.it.service;

import com.hau.ketnguyen.it.model.request.auth.UserRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchCategoryRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchFacultyRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchWorkplaceRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ExcelService {
    void exportCategory(SearchCategoryRequest request, HttpServletResponse response) throws Exception;
    void exportUser(UserRequest request, HttpServletResponse response) throws Exception;
    void exportFaculty(SearchFacultyRequest request, HttpServletResponse response) throws Exception;
    void exportWorkplace(SearchWorkplaceRequest request, HttpServletResponse response) throws Exception;
}
