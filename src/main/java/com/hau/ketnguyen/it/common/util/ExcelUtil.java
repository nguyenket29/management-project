package com.hau.ketnguyen.it.common.util;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.entity.auth.User;
import com.hau.ketnguyen.it.entity.hau.*;
import com.hau.ketnguyen.it.repository.auth.UserInfoReps;
import com.hau.ketnguyen.it.repository.auth.UserReps;
import com.hau.ketnguyen.it.repository.hau.LecturerReps;
import com.hau.ketnguyen.it.repository.hau.StudentReps;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static com.hau.ketnguyen.it.common.enums.TypeUser.LECTURE;

@Component
@AllArgsConstructor
public class ExcelUtil {
    private final UserInfoReps userInfoReps;
    private final LecturerReps lecturerReps;
    private final UserReps userReps;
    private final StudentReps studentReps;
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public boolean hasExcelFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    // import class
    @Transactional
    public List<Classes> excelToClasses(InputStream is, String SHEET) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<Classes> classes = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();

                Classes classEntity = new Classes();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            currentCell.setCellType(CellType.STRING);
                            classEntity.setCode(currentCell.getStringCellValue());
                            break;
                        case 1:
                            currentCell.setCellType(CellType.STRING);
                            classEntity.setName(currentCell.getStringCellValue());
                            break;
                        case 2:
                            currentCell.setCellType(CellType.STRING);
                            classEntity.setStdNumber(Integer.parseInt(currentCell.getStringCellValue()));
                            break;
                    }

                    classEntity.setFacultyId(Long.parseLong("5"));
                    cellIdx++;
                }
                classes.add(classEntity);
            }
            workbook.close();
            return classes;
        } catch (IOException e) {
            throw APIException.from(HttpStatus.BAD_REQUEST)
                    .withMessage("Fail to parse to Excel file: " + e.getMessage());
        }
    }

    // import faculty
    @Transactional
    public List<Faculties> excelToFaculty(InputStream is, String SHEET) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<Faculties> faculties = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();

                Faculties faculty = new Faculties();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            currentCell.setCellType(CellType.STRING);
                            faculty.setCode(currentCell.getStringCellValue());
                            break;
                        case 1:
                            currentCell.setCellType(CellType.STRING);
                            faculty.setName(currentCell.getStringCellValue());
                            break;
                        case 2:
                            currentCell.setCellType(CellType.STRING);
                            faculty.setSpecialization(currentCell.getStringCellValue());
                            break;
                    }

                    cellIdx++;
                }

                faculty.setWorkplaceId(Long.parseLong("3"));
                faculties.add(faculty);
            }
            workbook.close();
            return faculties;
        } catch (IOException e) {
            throw APIException.from(HttpStatus.BAD_REQUEST)
                    .withMessage("Fail to parse to Excel file: " + e.getMessage());
        }
    }

    // import student
    @Transactional
    public List<Students> excelToStudent(InputStream is, String SHEET) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<Students> students = new ArrayList<>();
            List<UserInfo> userInfos = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();

                UserInfo userInfo = new UserInfo();
                Students student = new Students();

                int cellIdx = 0;

                // tạo user info
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    userInfoReps.save(userInfo);

                    switch (cellIdx) {
                        case 0:
                            currentCell.setCellType(CellType.STRING);
                            student.setCodeStudent(currentCell.getStringCellValue());
                            break;
                        case 1:
                            currentCell.setCellType(CellType.STRING);
                            userInfo.setFullName(currentCell.getStringCellValue());
                            break;
                        case 2:
                            currentCell.setCellType(CellType.STRING);
                            userInfo.setAddress(currentCell.getStringCellValue());
                            break;
                        case 3:
                            currentCell.setCellType(CellType.STRING);
                            userInfo.setPhoneNumber(currentCell.getStringCellValue());
                            break;
                        case 4:
                            currentCell.setCellType(CellType.STRING);
                            userInfo.setGender(Short.parseShort(currentCell.getStringCellValue()));
                            break;
                    }

                    student.setClassId(Long.parseLong("7"));
                    student.setUserInfoId(userInfo.getId());
                    cellIdx++;
                }

                userInfos.add(userInfo);
                students.add(student);
            }
            workbook.close();

            userInfoReps.saveAll(userInfos);
            return students;
        } catch (IOException e) {
            throw APIException.from(HttpStatus.BAD_REQUEST)
                    .withMessage("Fail to parse to Excel file: " + e.getMessage());
        }
    }

    // import lecture
    @Transactional
    public List<Lecturers> excelToLecture(InputStream is, String SHEET) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<Lecturers> lecturers = new ArrayList<>();
            List<UserInfo> userInfos = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();

                UserInfo userInfo = new UserInfo();
                Lecturers lecturer = new Lecturers();

                int cellIdx = 0;

                // tạo user info
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    userInfoReps.save(userInfo);

                    switch (cellIdx) {
                        case 0:
                            currentCell.setCellType(CellType.STRING);
                            lecturer.setCodeLecture(currentCell.getStringCellValue());
                            break;
                        case 1:
                            currentCell.setCellType(CellType.STRING);
                            userInfo.setFullName(currentCell.getStringCellValue());
                            break;
                        case 2:
                            currentCell.setCellType(CellType.STRING);
                            userInfo.setAddress(currentCell.getStringCellValue());
                            break;
                        case 3:
                            currentCell.setCellType(CellType.STRING);
                            userInfo.setPhoneNumber(currentCell.getStringCellValue());
                            break;
                        case 4:
                            currentCell.setCellType(CellType.STRING);
                            userInfo.setGender(Short.parseShort(currentCell.getStringCellValue()));
                            break;
                        case 5:
                            currentCell.setCellType(CellType.STRING);
                            lecturer.setRegency(currentCell.getStringCellValue());
                            break;
                        case 6:
                            currentCell.setCellType(CellType.STRING);
                            lecturer.setDegree(currentCell.getStringCellValue());
                            break;
                    }

                    lecturer.setFacultyId(Long.parseLong("5"));
                    lecturer.setUserInfoId(userInfo.getId());
                    cellIdx++;
                }

                userInfos.add(userInfo);
                lecturers.add(lecturer);
            }
            workbook.close();

            userInfoReps.saveAll(userInfos);
            return lecturers;
        } catch (IOException e) {
            throw APIException.from(HttpStatus.BAD_REQUEST)
                    .withMessage("Fail to parse to Excel file: " + e.getMessage());
        }
    }

    // import topic
    @Transactional
    public List<Topics> excelToTopic(InputStream is, String SHEET) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<Topics> topics = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();

                Topics topic = new Topics();

                int cellIdx = 0;

                // tạo user info
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            currentCell.setCellType(CellType.STRING);
                            topic.setName(currentCell.getStringCellValue());
                            break;
                        case 1:
                            currentCell.setCellType(CellType.STRING);
                            topic.setStdNumber(Integer.parseInt(currentCell.getStringCellValue()));
                            break;
                        case 2:
                            currentCell.setCellType(CellType.STRING);
                            topic.setDescription(currentCell.getStringCellValue());
                            break;
                        case 3:
                            currentCell.setCellType(CellType.STRING);
                            topic.setScoreProcessOne(Float.valueOf(currentCell.getStringCellValue()));
                            break;
                        case 4:
                            currentCell.setCellType(CellType.STRING);
                            topic.setScoreProcessTwo(Float.valueOf(currentCell.getStringCellValue()));
                            break;
                        case 5:
                            currentCell.setCellType(CellType.STRING);
                            topic.setScoreAssembly(Float.valueOf(currentCell.getStringCellValue()));
                            break;
                        case 6:
                            currentCell.setCellType(CellType.STRING);
                            topic.setScoreGuide(Float.valueOf(currentCell.getStringCellValue()));
                            break;
                        case 7:
                            currentCell.setCellType(CellType.STRING);
                            topic.setScoreCounterArgument(Float.valueOf(currentCell.getStringCellValue()));
                            break;
                        case 8:
                            currentCell.setCellType(CellType.STRING);
                            UserInfo userInfo = userInfoReps.findByFullName(currentCell.getStringCellValue().toLowerCase())
                                            .orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy giảng viên"));
                            Lecturers lecturers = lecturerReps.findByUserInfoId(userInfo.getId())
                                    .orElseThrow(() -> APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy giảng viên"));
                            topic.setLecturerGuideId(lecturers.getId());
                            break;
                    }
                    cellIdx++;
                }
                topics.add(topic);
            }
            workbook.close();
            return topics;
        } catch (IOException e) {
            throw APIException.from(HttpStatus.BAD_REQUEST)
                    .withMessage("Fail to parse to Excel file: " + e.getMessage());
        }
    }

    // import user
    @Transactional
    public List<User> excelToUser(InputStream is, String SHEET) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<User> users = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();

                User user = new User();

                int cellIdx = 0;

                // tạo user info
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            currentCell.setCellType(CellType.STRING);
                            String typeAcc = currentCell.getStringCellValue();
                            if (Objects.equals(typeAcc, LECTURE.name())) {

                            }
                            break;
                    }
                    cellIdx++;
                }
                users.add(user);
            }
            workbook.close();
            return users;
        } catch (IOException e) {
            throw APIException.from(HttpStatus.BAD_REQUEST)
                    .withMessage("Fail to parse to Excel file: " + e.getMessage());
        }
    }
}
