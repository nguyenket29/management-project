package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.ExcelUtil;
import com.hau.ketnguyen.it.entity.hau.*;
import com.hau.ketnguyen.it.repository.auth.UserReps;
import com.hau.ketnguyen.it.repository.hau.*;
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
    private final TopicReps topicReps;
    private final WorkplaceReps workplaceReps;
    private final UserReps userReps;
    private final CategoryReps categoryReps;
    private final AssemblyReps assemblyReps;

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

    @Override
    public void importTopicExcel(MultipartFile file) {
        if (excelUtil.hasExcelFormat(file)) {
            try {
                List<Topics> topics = excelUtil.excelToTopic(file.getInputStream(), "Topic");
                if (!CollectionUtils.isEmpty(topics)) {
                    topicReps.saveAll(topics);
                }
            } catch (IOException e) {
                throw APIException.from(HttpStatus.BAD_REQUEST).withMessage("Import Excel Thất Bại");
            }
        }
    }

    @Override
    public void importTopicWorkplace(MultipartFile file) {
        if (excelUtil.hasExcelFormat(file)) {
            try {
                List<Workplaces> workplaces = excelUtil.excelToWorkplace(file.getInputStream(), "Workplace");
                if (!CollectionUtils.isEmpty(workplaces)) {
                    workplaceReps.saveAll(workplaces);
                }
            } catch (IOException e) {
                throw APIException.from(HttpStatus.BAD_REQUEST).withMessage("Import Excel Thất Bại");
            }
        }
    }

    @Override
    public void importTopicCategory(MultipartFile file) {
        if (excelUtil.hasExcelFormat(file)) {
            try {
                List<Categories> categories = excelUtil.excelToCategory(file.getInputStream(), "Category");
                if (!CollectionUtils.isEmpty(categories)) {
                    categoryReps.saveAll(categories);
                }
            } catch (IOException e) {
                throw APIException.from(HttpStatus.BAD_REQUEST).withMessage("Import Excel Thất Bại");
            }
        }
    }

    @Override
    public void importTopicAssembly(MultipartFile file) {
        if (excelUtil.hasExcelFormat(file)) {
            try {
                List<Assemblies> assemblies = excelUtil.excelToAssembly(file.getInputStream(), "Assembly");
                if (!CollectionUtils.isEmpty(assemblies)) {
                    assemblyReps.saveAll(assemblies);
                }
            } catch (IOException e) {
                throw APIException.from(HttpStatus.BAD_REQUEST).withMessage("Import Excel Thất Bại");
            }
        }
    }

    @Override
    public void importExcelUser(MultipartFile file) {
        if (excelUtil.hasExcelFormat(file)) {
            try {
                excelUtil.excelToAssembly(file.getInputStream(), "User");
            } catch (IOException e) {
                throw APIException.from(HttpStatus.BAD_REQUEST).withMessage("Import Excel Thất Bại");
            }
        }
    }
}
