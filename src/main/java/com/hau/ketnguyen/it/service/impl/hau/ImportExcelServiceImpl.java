package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.ExcelUtil;
import com.hau.ketnguyen.it.entity.hau.Classes;
import com.hau.ketnguyen.it.entity.hau.Faculties;
import com.hau.ketnguyen.it.entity.hau.Lecturers;
import com.hau.ketnguyen.it.entity.hau.Students;
import com.hau.ketnguyen.it.repository.hau.ClassReps;
import com.hau.ketnguyen.it.repository.hau.FacultyReps;
import com.hau.ketnguyen.it.repository.hau.LecturerReps;
import com.hau.ketnguyen.it.repository.hau.StudentReps;
import com.hau.ketnguyen.it.service.ImportExcelService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class ImportExcelServiceImpl implements ImportExcelService {
    private final ClassReps classReps;
    private final FacultyReps facultyReps;
    private final StudentReps studentReps;
    private final LecturerReps lecturerReps;
    private final ExcelUtil excelUtil;

    @Override
    public void importClassExcel(MultipartFile file) {
        if (excelUtil.hasExcelFormat(file)) {
            try {
                List<Classes> classesList = excelUtil.excelToClasses(file.getInputStream(), "Class");
                if (!CollectionUtils.isEmpty(classesList)) {
                    classReps.saveAll(classesList);
                }
            } catch (IOException e) {
                throw APIException.from(HttpStatus.BAD_REQUEST).withMessage("Import Excel Thất Bại");
            }
        }
    }

    @Override
    public void importFacultyExcel(MultipartFile file) {
        if (excelUtil.hasExcelFormat(file)) {
            try {
                List<Faculties> faculties = excelUtil.excelToFaculty(file.getInputStream(), "Faculty");
                if (!CollectionUtils.isEmpty(faculties)) {
                    facultyReps.saveAll(faculties);
                }
            } catch (IOException e) {
                throw APIException.from(HttpStatus.BAD_REQUEST).withMessage("Import Excel Thất Bại");
            }
        }
    }

    @Override
    public void importStudentExcel(MultipartFile file) {
        if (excelUtil.hasExcelFormat(file)) {
            try {
                List<Students> students = excelUtil.excelToStudent(file.getInputStream(), "Student");
                if (!CollectionUtils.isEmpty(students)) {
                    studentReps.saveAll(students);
                }
            } catch (IOException e) {
                throw APIException.from(HttpStatus.BAD_REQUEST).withMessage("Import Excel Thất Bại");
            }
        }
    }

    @Override
    public void importLectureExcel(MultipartFile file) {
        if (excelUtil.hasExcelFormat(file)) {
            try {
                List<Lecturers> lecturers = excelUtil.excelToLecture(file.getInputStream(), "Lecture");
                if (!CollectionUtils.isEmpty(lecturers)) {
                    lecturerReps.saveAll(lecturers);
                }
            } catch (IOException e) {
                throw APIException.from(HttpStatus.BAD_REQUEST).withMessage("Import Excel Thất Bại");
            }
        }
    }
}
