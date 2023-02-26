package com.hau.ketnguyen.it.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.entity.auth.User;
import com.hau.ketnguyen.it.entity.hau.*;
import com.hau.ketnguyen.it.repository.auth.UserInfoReps;
import com.hau.ketnguyen.it.repository.auth.UserReps;
import com.hau.ketnguyen.it.repository.hau.LecturerReps;
import com.hau.ketnguyen.it.repository.hau.StudentReps;
import com.hau.ketnguyen.it.repository.hau.TopicReps;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.hau.ketnguyen.it.common.enums.TypeUser.*;
import static com.hau.ketnguyen.it.entity.auth.User.Status.ACTIVE;

@Component
@AllArgsConstructor
public class ExcelUtil {
    private final UserInfoReps userInfoReps;
    private final LecturerReps lecturerReps;
    private final UserReps userReps;
    private final StudentReps studentReps;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TopicReps topicReps;
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

    // import workplace
    @Transactional
    public List<Workplaces> excelToWorkplace(InputStream is, String SHEET) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<Workplaces> workplaces = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();

                Workplaces workplace = new Workplaces();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            currentCell.setCellType(CellType.STRING);
                            workplace.setName(currentCell.getStringCellValue());
                            break;
                        case 1:
                            currentCell.setCellType(CellType.STRING);
                            workplace.setAddress(currentCell.getStringCellValue());
                            break;
                        case 2:
                            currentCell.setCellType(CellType.STRING);
                            workplace.setEmail(currentCell.getStringCellValue());
                            break;
                        case 3:
                            currentCell.setCellType(CellType.STRING);
                            workplace.setPhoneNumber(currentCell.getStringCellValue());
                            break;
                    }

                    cellIdx++;
                }
                workplaces.add(workplace);
            }
            workbook.close();
            return workplaces;
        } catch (IOException e) {
            throw APIException.from(HttpStatus.BAD_REQUEST)
                    .withMessage("Fail to parse to Excel file: " + e.getMessage());
        }
    }

    // import category
    @Transactional
    public List<Categories> excelToCategory(InputStream is, String SHEET) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<Categories> categories = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();

                Categories category = new Categories();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            currentCell.setCellType(CellType.STRING);
                            category.setName(currentCell.getStringCellValue());
                            break;
                        case 1:
                            currentCell.setCellType(CellType.STRING);
                            category.setCode(currentCell.getStringCellValue());
                            break;
                        case 2:
                            currentCell.setCellType(CellType.STRING);
                            category.setDescription(currentCell.getStringCellValue());
                            break;
                    }

                    cellIdx++;
                }
                categories.add(category);
            }
            workbook.close();
            return categories;
        } catch (IOException e) {
            throw APIException.from(HttpStatus.BAD_REQUEST)
                    .withMessage("Fail to parse to Excel file: " + e.getMessage());
        }
    }

    // import assembly
    @Transactional
    public List<Assemblies> excelToAssembly(InputStream is, String SHEET) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<Assemblies> assemblies = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }
                Iterator<Cell> cellsInRow = currentRow.iterator();

                Assemblies assembly = new Assemblies();
                ObjectMapper objectMapper = new ObjectMapper();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            currentCell.setCellType(CellType.STRING);
                            assembly.setNameAssembly(currentCell.getStringCellValue());
                            break;
                        case 1:
                            currentCell.setCellType(CellType.STRING);
                            assembly.setLecturePresidentId(getLectureId(currentCell.getStringCellValue()));
                            break;
                        case 2:
                            currentCell.setCellType(CellType.STRING);
                            assembly.setLectureSecretaryId(getLectureId(currentCell.getStringCellValue()));
                            break;
                        case 3:
                            currentCell.setCellType(CellType.STRING);
                            String lectureNames = currentCell.getStringCellValue();

                            List<String> listName = splitByRegex(lectureNames);
                            List<UserInfo> userInfos = userInfoReps.findAllByFullNameIn(listName);
                            if (!CollectionUtils.isEmpty(userInfos)) {
                                List<Long> userInfoIds = userInfos.stream().map(UserInfo::getId).collect(Collectors.toList());
                                if (!CollectionUtils.isEmpty(userInfoIds)) {
                                    List<Lecturers> lecturers = lecturerReps.findAllByUserInfoIdIn(userInfoIds);
                                    if (!CollectionUtils.isEmpty(lecturers)) {
                                        List<Long> lectureIds = lecturers.stream().map(Lecturers::getId).collect(Collectors.toList());
                                        String lectureIdList = objectMapper.writeValueAsString(lectureIds);
                                        assembly.setLecturerIds(lectureIdList);
                                    }
                                }
                            }
                            break;
                        case 4:
                            currentCell.setCellType(CellType.STRING);
                            String topicNames = currentCell.getStringCellValue();

                            List<String> nameTopicList = splitByRegex(topicNames);
                            if (!CollectionUtils.isEmpty(nameTopicList)) {
                                List<Topics> topics = topicReps.findByNameIn(nameTopicList);
                                if (!CollectionUtils.isEmpty(topics)) {
                                    List<Long> topicIds = topics.stream().map(Topics::getId).collect(Collectors.toList());
                                    assembly.setTopicIds(objectMapper.writeValueAsString(topicIds));
                                }
                            }
                            break;
                    }

                    cellIdx++;
                }
                assemblies.add(assembly);
            }
            workbook.close();
            return assemblies;
        } catch (IOException e) {
            throw APIException.from(HttpStatus.BAD_REQUEST)
                    .withMessage("Fail to parse to Excel file: " + e.getMessage());
        }
    }

    private List<String> splitByRegex(String value) {
        List<String> list = new ArrayList<>();
        if (!StringUtil.isEmpty(value)) {
            Arrays.stream(value.split(",")).forEach(i -> list.add(i.toLowerCase().trim()));
        }
        return list;
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
                        case 5:
                            currentCell.setCellType(CellType.STRING);
                            userInfo.setTown(currentCell.getStringCellValue());
                            break;
                    }

                    validateLecture(student.getCodeStudent(), userInfo.getFullName());
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
                        case 7:
                            currentCell.setCellType(CellType.STRING);
                            userInfo.setTown(currentCell.getStringCellValue());
                            break;
                    }

                    validateLecture(lecturer.getCodeLecture(), userInfo.getFullName());
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
                            topic.setLecturerGuideId(getLectureId(currentCell.getStringCellValue()));
                            break;
                    }

                    topic.setStatusSuggest(true);
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

    private Long getLectureId(String value) {
        UserInfo userInfo = new UserInfo();
        Optional<UserInfo> userInfoOptional = userInfoReps.findByFullName(value.toLowerCase());
        if (userInfoOptional.isPresent()) {
            userInfo = userInfoOptional.get();
        }

        Lecturers lecturers = new Lecturers();
        Optional<Lecturers> lecturersOptional = lecturerReps.findByUserInfoId(userInfo.getId());
        if (lecturersOptional.isPresent()) {
            lecturers = lecturersOptional.get();
        }

        return lecturers.getId();
    }

    // import user
    @Transactional
    public List<User> excelToUser(InputStream is, String SHEET) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();
            List<User> users = new ArrayList<>();
            List<Students> students = new ArrayList<>();
            List<Lecturers> lecturers = new ArrayList<>();
            Map<Integer, String> codeStudents = new HashMap<>();
            Map<Integer, String> codeLectures = new HashMap<>();
            List<UserInfo> userInfos = new ArrayList<>();

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

                    user.setStatus(ACTIVE);
                    switch (cellIdx) {
                        case 0:
                            currentCell.setCellType(CellType.STRING);
                            String typeAcc = currentCell.getStringCellValue();
                            if (LECTURE.name().equalsIgnoreCase(typeAcc)) {
                                user.setType(LECTURE.name());
                            } else if (STUDENT.name().equalsIgnoreCase(typeAcc)) {
                                user.setType(STUDENT.name());
                            } else if (OTHER.name().equalsIgnoreCase(typeAcc)) {
                                user.setType(OTHER.name());
                            }
                            break;
                        case 1:
                            currentCell.setCellType(CellType.STRING);
                            user.setUsername(currentCell.getStringCellValue());
                            break;
                        case 2:
                            currentCell.setCellType(CellType.STRING);
                            user.setEmail(currentCell.getStringCellValue());
                            break;
                        case 3:
                            currentCell.setCellType(CellType.STRING);
                            user.setPassword(bCryptPasswordEncoder.encode(currentCell.getStringCellValue()));
                            break;
                        case 4:
                            currentCell.setCellType(CellType.STRING);
                            validateAcc(user.getUsername(), user.getEmail());
                            String code = currentCell.getStringCellValue().toLowerCase();
                            if (!StringUtils.isNullOrEmpty(code)) {
                                if (LECTURE.name().equalsIgnoreCase(user.getType())) {
                                    userReps.save(user);
                                    codeLectures.put(user.getId(), code);
                                } else if (STUDENT.name().equalsIgnoreCase(user.getType())) {
                                    userReps.save(user);
                                    codeStudents.put(user.getId(), code);
                                } else if (OTHER.name().equalsIgnoreCase(user.getType())) {
                                    userReps.save(user);
                                    UserInfo userInfo = new UserInfo();
                                    userInfo.setUserId(user.getId());
                                    userInfos.add(userInfo);
                                }
                            }
                            break;
                    }

                    cellIdx++;
                }
                users.add(user);
            }

            // cập nhật sinh viên
            if (!CollectionUtils.isEmpty(codeStudents)) {
                Map<String, Students> mapStudent = getStudentIds(new ArrayList<>(codeStudents.values()));
                if (!CollectionUtils.isEmpty(mapStudent) && !CollectionUtils.isEmpty(codeStudents)) {
                    mapStudent.forEach((k, v) -> codeStudents.forEach((i, j) -> {
                        if (Objects.equals(k, j)) {
                            v.setUserId(i);
                            students.add(v);
                        }
                    }));
                }

                if (!CollectionUtils.isEmpty(students)) {
                    studentReps.saveAll(students);
                }
            }

            // cập nhật giảng viên
            if (!CollectionUtils.isEmpty(codeLectures)) {
                Map<String, Lecturers> mapLecture = getLectureIds(new ArrayList<>(codeLectures.values()));
                if (!CollectionUtils.isEmpty(mapLecture) && !CollectionUtils.isEmpty(codeStudents)) {
                    mapLecture.forEach((k, v) -> codeLectures.forEach((i, j) -> {
                        if (Objects.equals(k, j)) {
                            v.setUserId(i);
                            lecturers.add(v);
                        }
                    }));
                }

                if (!CollectionUtils.isEmpty(lecturers)) {
                    lecturerReps.saveAll(lecturers);
                }
            }

            // cập nhật quản trị viên
            if (!CollectionUtils.isEmpty(userInfos)) {
                userInfoReps.saveAll(userInfos);
            }

            workbook.close();
            return users;
        } catch (IOException e) {
            throw APIException.from(HttpStatus.BAD_REQUEST)
                    .withMessage("Fail to parse to Excel file: " + e.getMessage());
        }
    }

    private void validateAcc(String username, String email) {
        List<User> users = userReps.findAllByUsernameOrEmail(username, email);
        if (!CollectionUtils.isEmpty(users)) {
            throw APIException.from(HttpStatus.CONFLICT).withMessage("Tài khoản đã tồn tại");
        }
    }

    private Map<String, Students> getStudentIds(List<String> codes) {
        Map<String, Students> map =
                studentReps.findByCodeStudentIn(codes).stream().collect(Collectors.toMap(Students::getCodeStudent, l -> l));
        if (!CollectionUtils.isEmpty(map)) {
            return map;
        }
        return new HashMap<>();
    }

    private Map<String, Lecturers> getLectureIds(List<String> codes) {
        Map<String, Lecturers> map =
                lecturerReps.findByCodeLectureIn(codes).stream().collect(Collectors.toMap(Lecturers::getCodeLecture, l -> l));
        if (!CollectionUtils.isEmpty(map)) {
            return map;
        }
        return new HashMap<>();
    }

    private void validateLecture(String code, String fullName) {
        Optional<Lecturers> lecturers = lecturerReps.findByCodeLecture(code);
        Optional<UserInfo> userInfo = userInfoReps.findByFullName(fullName);
        if (lecturers.isPresent() || userInfo.isPresent()) {
            throw APIException.from(HttpStatus.CONFLICT).withMessage("Giảng viên này đã tồn tại trên hệ thống");
        }
    }

    private void validateStudent(String code, String fullName) {
        Optional<Students> students = studentReps.findByCodeStudent(code);
        Optional<UserInfo> userInfo = userInfoReps.findByFullName(fullName);
        if (students.isPresent() || userInfo.isPresent()) {
            throw APIException.from(HttpStatus.CONFLICT).withMessage("Sinh viên này đã tồn tại trên hệ thống");
        }
    }
}
