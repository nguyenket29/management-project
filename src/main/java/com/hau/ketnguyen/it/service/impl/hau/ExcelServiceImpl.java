package com.hau.ketnguyen.it.service.impl.hau;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hau.ketnguyen.it.common.anotations.Reflections;
import com.hau.ketnguyen.it.common.util.ExportExcelUtil;
import com.hau.ketnguyen.it.common.util.HashHelper;
import com.hau.ketnguyen.it.common.util.StringUtils;
import com.hau.ketnguyen.it.entity.hau.Categories;
import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import com.hau.ketnguyen.it.model.dto.auth.UserInfoDTO;
import com.hau.ketnguyen.it.model.dto.excel.*;
import com.hau.ketnguyen.it.model.dto.hau.*;
import com.hau.ketnguyen.it.model.request.auth.UserRequest;
import com.hau.ketnguyen.it.model.request.hau.*;
import com.hau.ketnguyen.it.repository.auth.UserInfoReps;
import com.hau.ketnguyen.it.repository.auth.UserReps;
import com.hau.ketnguyen.it.repository.hau.*;
import com.hau.ketnguyen.it.service.*;
import com.hau.ketnguyen.it.service.mapper.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static com.hau.ketnguyen.it.service.impl.hau.AssemblyServiceImpl.getLongLecturerDTOMap;

@Slf4j
@Service
@AllArgsConstructor
public class ExcelServiceImpl implements ExcelService {
    public static String type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private final UserInfoReps userInfoReps;
    private final UserInfoMapper userInfoMapper;
    private final WorkplaceService workplaceService;
    private final WorkplaceMapper workplaceMapper;
    private final WorkplaceReps workplaceReps;
    private final FacultyService facultyService;
    private final FacultyMapper facultyMapper;
    private final FacultyReps facultyReps;
    private final UserService userService;
    private final UserReps userReps;
    private final UserMapper userMapper;
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final CategoryReps categoryReps;
    private final ClassService classService;
    private final ClassMapper classMapper;
    private final ClassReps classReps;
    private final LecturerReps lecturerReps;
    private final LecturerMapper lecturerMapper;
    private final LecturerService lecturerService;
    private final AssemblyService assemblyService;
    private final AssemblyReps assemblyReps;
    private final AssemblyMapper assemblyMapper;
    private final StudentReps studentReps;
    private final StudentMapper studentMapper;
    private final StudentService studentService;
    private final TopicReps topicReps;
    private final TopicMapper topicMapper;
    private final TopicService topicService;

    @Override
    public byte[] exportCategory(SearchCategoryRequest request, HttpServletResponse response) throws Exception {
        long count = categoryReps.count();
        request.setSize((int) count);
        List<CategoryDTO> categoryDTOS = categoryService.getAll(request).getData()
                .stream().sorted(Comparator.comparingLong(CategoryDTO::getId)).collect(Collectors.toList());
        List<CategoryExcelDTO> categoryExcelDTOS = categoryMapper.toExcel(categoryDTOS);
        List<String> headerListNew = Arrays.asList("Mã chủ đề", "Tên chủ đề");

        return exportExcel(response, "Export Category",
                "Category", "category_export", CategoryExcelDTO.class.getDeclaredFields(),
                Arrays.asList(categoryExcelDTOS.toArray()), headerListNew);
    }

    @Override
    public void exportUser(UserRequest request, HttpServletResponse response) throws Exception {
        long count = userReps.count();
        request.setSize((int) count);
        List<UserDTO> userDTOS = userService.getAll(request).getData()
                .stream().sorted(Comparator.comparingLong(UserDTO::getId)).collect(Collectors.toList());
        List<UserExcelDTO> userExcelDTOS = userMapper.toExcel(userDTOS);

        if (!CollectionUtils.isEmpty(userDTOS) && !CollectionUtils.isEmpty(userExcelDTOS)) {
            userDTOS.forEach(u -> userExcelDTOS.forEach(ux -> {
                if (Objects.equals(u.getUsername(), ux.getUsername())
                        && Objects.equals(u.getEmail(), ux.getEmail())) {
                    ux.setStatus(getMapStatus().get(u.getStatus()));
                    ux.setType(getTypeAcc().get(u.getType()));
                }
            }));
        }

        List<String> headerListNew = Arrays.asList("Tên người dùng", "Email", "Quyền hạn", "Trạng thái", "Loại tài khoản");

        exportExcel(response, "Export User",
                "User", "user_export", UserExcelDTO.class.getDeclaredFields(),
                Arrays.asList(userExcelDTOS.toArray()), headerListNew);
    }

    @Override
    public void exportFaculty(SearchFacultyRequest request, HttpServletResponse response) throws Exception {
        long count = facultyReps.count();
        request.setSize((int) count);
        List<FacultyDTO> facultyDTOS = facultyService.getAll(request).getData()
                .stream().sorted(Comparator.comparingLong(FacultyDTO::getId)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(facultyDTOS)) {
            List<FacultyExcelDTO> facultyExcelDTOS = facultyMapper.toExcel(facultyDTOS);
            List<Long> workplaceIds = facultyDTOS.stream().map(FacultyDTO::getWorkplaceId).collect(Collectors.toList());
            Map<Long, WorkplaceDTO> workplaceDTOMap = workplaceReps.findByIdIn(workplaceIds)
                    .stream().map(workplaceMapper::to).collect(Collectors.toMap(WorkplaceDTO::getId, w -> w));

            if (!CollectionUtils.isEmpty(facultyExcelDTOS)) {
                facultyDTOS.forEach(f -> facultyExcelDTOS.forEach(fx -> {
                    if (Objects.equals(f.getName(), fx.getName()) &&
                            Objects.equals(f.getSpecialization(), fx.getSpecialization())) {
                        if (f.getWorkplaceId() != null && !CollectionUtils.isEmpty(workplaceDTOMap) && workplaceDTOMap.containsKey(f.getWorkplaceId())) {
                            fx.setWorkplace(workplaceDTOMap.get(f.getWorkplaceId()).getName());
                        }
                    }
                }));
            }

            List<String> headerListNew = Arrays.asList("Tên khoa", "Mã Khoa", "Chuyên ngành", "Đơn vị");

            exportExcel(response, "Export Faculty",
                    "Faculty", "faculty_export", FacultyExcelDTO.class.getDeclaredFields(),
                    Arrays.asList(facultyExcelDTOS.toArray()), headerListNew);
        }
    }

    @Override
    public void exportWorkplace(SearchWorkplaceRequest request, HttpServletResponse response) throws Exception {
        long count = workplaceReps.count();
        request.setSize((int) count);
        List<WorkplaceDTO> workplaceDTOS = workplaceService.getAll(request).getData()
                .stream().sorted(Comparator.comparingLong(WorkplaceDTO::getId)).collect(Collectors.toList());
        List<WorkplaceExcelDTO> workplaceExcelDTOS = workplaceMapper.toExcel(workplaceDTOS);

        List<String> headerListNew = Arrays.asList("Nơi làm việc", "Email", "Số điện thoại", "Địa chỉ");

        exportExcel(response, "Export Workplace",
                "Workplace", "workplace_export", WorkplaceExcelDTO.class.getDeclaredFields(),
                Arrays.asList(workplaceExcelDTOS.toArray()), headerListNew);
    }

    @Override
    public void exportClass(SearchClassRequest request, HttpServletResponse response) throws Exception {
        long count = classReps.count();
        request.setSize((int) count);
        List<ClassDTO> classDTOS = classService.getAll(request).getData()
                .stream().sorted(Comparator.comparingLong(ClassDTO::getId)).collect(Collectors.toList());
        List<ClassExcelDTO> classExcelDTOS = classMapper.toExcel(classDTOS);

        if (!CollectionUtils.isEmpty(classDTOS)) {
            List<Long> facultyIds = classDTOS.stream().map(ClassDTO::getFacultyId).collect(Collectors.toList());
            Map<Long, FacultyDTO> facultyDTOMap = facultyReps.findByIdIn(facultyIds)
                    .stream().map(facultyMapper::to).collect(Collectors.toMap(FacultyDTO::getId, f -> f));

            if (!CollectionUtils.isEmpty(classExcelDTOS)) {
                classDTOS.forEach(c -> classExcelDTOS.forEach(cx -> {
                    if (Objects.equals(c.getCode(), cx.getCode())) {
                        if (!CollectionUtils.isEmpty(facultyDTOMap) && facultyDTOMap.containsKey(c.getFacultyId())) {
                            cx.setFacultyName(facultyDTOMap.get(c.getFacultyId()).getName());
                        }
                    }
                }));
            }

            List<String> headerListNew = Arrays.asList("Mã Lớp", "Tên Lớp", "Khoa", "Số lượng");

            exportExcel(response, "Export Class",
                    "Class", "class_export", ClassExcelDTO.class.getDeclaredFields(),
                    Arrays.asList(classExcelDTOS.toArray()), headerListNew);
        }
    }

    public Map<Short, String> getMapGender() {
        Map<Short, String> mapStatus = new HashMap<>();
        mapStatus.put((short) 0, "Nam");
        mapStatus.put((short) 1, "Nữ");
        mapStatus.put((short) 2, "Khác");
        return mapStatus;
    }

    @Override
    public void exportLecture(SearchLecturerRequest request, HttpServletResponse response) throws Exception {
        long count = lecturerReps.count();
        request.setSize((int) count);
        List<LecturerDTO> lecturerDTOS = lecturerService.getAll(request).getData()
                .stream().sorted(Comparator.comparingLong(LecturerDTO::getId)).collect(Collectors.toList());
        List<LectureExcelDTO> lectureExcelDTOS = lecturerMapper.toExcel(lecturerDTOS);

        if (!CollectionUtils.isEmpty(lecturerDTOS)) {
            List<Long> userInfoIds = lecturerDTOS.stream().map(LecturerDTO::getUserInfoId).collect(Collectors.toList());
            List<Long> facultyIds = lecturerDTOS.stream().map(LecturerDTO::getFacultyId).collect(Collectors.toList());

            Map<Long, UserInfoDTO> userInfoDTOMap = userInfoReps.findByIdIn(userInfoIds).stream()
                    .map(userInfoMapper::to).collect(Collectors.toMap(UserInfoDTO::getId, u -> u));
            Map<Long, FacultyDTO> facultyDTOMap = facultyReps.findByIdIn(facultyIds).stream()
                    .map(facultyMapper::to).collect(Collectors.toMap(FacultyDTO::getId, u -> u));

            if (!CollectionUtils.isEmpty(lectureExcelDTOS)) {
                lecturerDTOS.forEach(l -> lectureExcelDTOS.forEach(lx -> {
                    if (Objects.equals(l.getCodeLecture(), lx.getCodeLecture())) {
                        if (l.getUserInfoId() != null && !CollectionUtils.isEmpty(userInfoDTOMap)
                                && userInfoDTOMap.containsKey(l.getUserInfoId())) {
                            lx.setFullName(userInfoDTOMap.get(l.getUserInfoId()).getFullName());
                            lx.setGender(getMapGender().get(userInfoDTOMap.get(l.getUserInfoId()).getGender()));
                            lx.setDateOfBirth(userInfoDTOMap.get(l.getUserInfoId()).getDateOfBirth());
                            lx.setAddress(userInfoDTOMap.get(l.getUserInfoId()).getAddress());
                            lx.setTown(userInfoDTOMap.get(l.getUserInfoId()).getTown());
                        }

                        if (l.getFacultyId() != null && !CollectionUtils.isEmpty(facultyDTOMap)
                                && facultyDTOMap.containsKey(l.getFacultyId())) {
                            lx.setFacultyName(facultyDTOMap.get(l.getFacultyId()).getName());
                        }
                    }
                }));
            }

            List<String> headerListNew = Arrays.asList("Mã giáo viên", "Họ và tên", "Giới tính", "Ngày sinh",
                    "Địa chỉ", "Bằng cấp", "Đơn vị", "Chức vụ", "Quê quán");

            exportExcel(response, "Export Lecture",
                    "Danh sách giảng viên", "lecture_export", LectureExcelDTO.class.getDeclaredFields(),
                    Arrays.asList(lectureExcelDTOS.toArray()), headerListNew);
        }
    }

    @Override
    public void exportAssembly(SearchAssemblyRequest request, HttpServletResponse response) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        long count = assemblyReps.count();
        request.setSize((int) count);
        List<AssemblyDTO> assemblyDTOS = assemblyService.getAll(request).getData()
                .stream().sorted(Comparator.comparingLong(AssemblyDTO::getId)).collect(Collectors.toList());
        List<AssemblyExcelDTO> assemblyExcelDTOS = assemblyMapper.toExcel(assemblyDTOS);

        if (!CollectionUtils.isEmpty(assemblyDTOS)) {
            List<Long> topicIds = assemblyDTOS.stream().map(AssemblyDTO::getTopicId).collect(Collectors.toList());
            Map<Long, TopicDTO> topicDTOMap = topicReps.findByIdIn(topicIds)
                    .stream().map(topicMapper::to).collect(Collectors.toMap(TopicDTO::getId, t -> t));

            Map<Long, List<Long>> mapTopicLectrueIds = new HashMap<>();
            List<Long> idLectures = new ArrayList<>();
            assemblyDTOS.forEach(l -> {
                List<Integer> lectureIds = null;
                try {
                    if (!StringUtils.isNullOrEmpty(l.getLecturerIds())) {
                        lectureIds = objectMapper.readValue(l.getLecturerIds(), List.class);
                    }
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                List<Long> lectureIdLong = new ArrayList<>();

                if (lectureIds != null && !lectureIds.isEmpty()) {
                    lectureIds.forEach(i -> lectureIdLong.add(Long.parseLong(i.toString())));
                }

                idLectures.addAll(lectureIdLong);
                mapTopicLectrueIds.put(l.getId(), lectureIdLong);
            });

            Map<Long, LecturerDTO> lecturerDTOMap = setLecture(idLectures);
            Map<Long, List<LecturerDTO>> mapTopicWithLectureDTO = new HashMap<>();
            if (!mapTopicLectrueIds.isEmpty()) {
                mapTopicLectrueIds.forEach((k, v) -> {
                    if (v != null && !v.isEmpty()) {
                        List<LecturerDTO> list = new ArrayList<>();
                        v.forEach(i -> {
                            if (!lecturerDTOMap.isEmpty() && lecturerDTOMap.containsKey(i)) {
                                list.add(lecturerDTOMap.get(i));
                            }
                        });
                        mapTopicWithLectureDTO.put(k, list);
                    }
                });
            }

            if (!CollectionUtils.isEmpty(assemblyExcelDTOS)) {
                assemblyDTOS.forEach(a -> assemblyExcelDTOS.forEach(ax -> {
                    if (Objects.equals(a.getNameAssembly(), ax.getNameAssembly())) {
                        if (a.getTopicId() != null && !topicDTOMap.isEmpty() && topicDTOMap.containsKey(a.getTopicId())) {
                            ax.setTopicName(topicDTOMap.get(a.getTopicId()).getName());
                        }

                        if (a.getId() != null && !mapTopicWithLectureDTO.isEmpty() && mapTopicWithLectureDTO.containsKey(a.getId())) {
                            List<String> lecturerNames = mapTopicWithLectureDTO.get(a.getId())
                                    .stream().map(LecturerDTO::getFullName).collect(Collectors.toList());
                            ax.setLecturerNames(lecturerNames);
                        }
                    }
                }));
            }

            List<String> headerListNew = Arrays.asList("Tên hội đồng", "Giáo viên", "Đồ án", "Điểm");

            exportExcel(response, "Export Assembly",
                    "Danh sách hội đồng", "assembly_export", AssemblyExcelDTO.class.getDeclaredFields(),
                    Arrays.asList(assemblyExcelDTOS.toArray()), headerListNew);
        }
    }

    private Map<Long, LecturerDTO> setLecture(List<Long> lectureIds) {
        return getLongLecturerDTOMap(lectureIds, lecturerReps, lecturerMapper, userInfoReps);
    }

    @Override
    public void exportStudent(SearchStudentRequest request, HttpServletResponse response) throws Exception {
        long count = studentReps.count();
        request.setSize((int) count);
        List<StudentDTO> studentDTOS = studentService.getAll(request).getData()
                .stream().sorted(Comparator.comparingLong(StudentDTO::getId)).collect(Collectors.toList());
        List<StudentExcelDTO> studentExcelDTOS = studentMapper.toExcel(studentDTOS);

        if (!CollectionUtils.isEmpty(studentDTOS)) {
            List<Long> userInfoIds = studentDTOS.stream().map(StudentDTO::getUserInfoId).collect(Collectors.toList());
            List<Long> topicIds = studentDTOS.stream().map(StudentDTO::getClassId).collect(Collectors.toList());
            List<Long> classIds = studentDTOS.stream().map(StudentDTO::getTopicId).collect(Collectors.toList());

            Map<Long, UserInfoDTO> userInfoDTOMap = userInfoReps.findByIdIn(userInfoIds).stream()
                    .map(userInfoMapper::to).collect(Collectors.toMap(UserInfoDTO::getId, u -> u));
            Map<Long, ClassDTO> classDTOMap = classReps.findByIdIn(classIds).stream()
                    .map(classMapper::to).collect(Collectors.toMap(ClassDTO::getId, u -> u));
            Map<Long, TopicDTO> topicDTOMap = topicReps.findByIdIn(topicIds).stream()
                    .map(topicMapper::to).collect(Collectors.toMap(TopicDTO::getId, u -> u));

            if (!CollectionUtils.isEmpty(studentExcelDTOS)) {
                studentDTOS.forEach(s -> studentExcelDTOS.forEach(sx -> {
                    if (Objects.equals(s.getCodeStudent(), sx.getCodeStudent())) {
                        if (s.getUserInfoId() != null && !CollectionUtils.isEmpty(userInfoDTOMap) && userInfoDTOMap.containsKey(s.getUserInfoId())) {
                            sx.setFullName(userInfoDTOMap.get(s.getUserInfoId()).getFullName());
                            sx.setGender(getMapGender().get(userInfoDTOMap.get(s.getUserInfoId()).getGender()));
                            sx.setDateOfBirth(userInfoDTOMap.get(s.getUserInfoId()).getDateOfBirth());
                            sx.setAddress(userInfoDTOMap.get(s.getUserInfoId()).getAddress());
                            sx.setTown(userInfoDTOMap.get(s.getUserInfoId()).getTown());
                        }

                        if (s.getClassId() != null && !CollectionUtils.isEmpty(classDTOMap) && classDTOMap.containsKey(s.getClassId())) {
                            sx.setClassName(classDTOMap.get(s.getClassId()).getName());
                        }

                        if (s.getTopicId() != null && !CollectionUtils.isEmpty(topicDTOMap) && topicDTOMap.containsKey(s.getTopicId())) {
                            sx.setTopicName(topicDTOMap.get(s.getTopicId()).getName());
                        }
                    }
                }));
            }

            List<String> headerListNew = Arrays.asList("Mã sinh viên", "Họ và tên", "Giới tính",
                    "Ngày sinh", "Địa chỉ", "Quê quán", "Lớp", "Đề tài");

            exportExcel(response, "Export Student",
                    "Danh sách sinh viên", "student_export", StudentExcelDTO.class.getDeclaredFields(),
                    Arrays.asList(studentExcelDTOS.toArray()), headerListNew);
        }
    }

    @Override
    public void exportTopic(SearchTopicRequest request, HttpServletResponse response) throws Exception {
        long count = topicReps.count();
        request.setSize((int) count);
        List<TopicDTO> topicDTOS = topicService.getAll(request).getData()
                .stream().sorted(Comparator.comparingLong(TopicDTO::getId)).collect(Collectors.toList());
        List<TopicExcelDTO> topicExcelDTOS = topicMapper.toExcel(topicDTOS);

        if (!CollectionUtils.isEmpty(topicDTOS)) {
            List<Long> categoryIds = topicDTOS.stream().map(TopicDTO::getCategoryId).collect(Collectors.toList());
            List<Long> lectureGuideIds = topicDTOS.stream()
                    .map(TopicDTO::getLecturerGuideId).distinct().collect(Collectors.toList());
            List<Long> lectureCounterArgumentIds = topicDTOS.stream()
                    .map(TopicDTO::getLecturerCounterArgumentId).distinct().collect(Collectors.toList());
            List<Long> lectureIds = new ArrayList<>();
            lectureIds.addAll(lectureGuideIds);
            lectureIds.addAll(lectureCounterArgumentIds);

            Map<Long, LecturerDTO> lecturerDTOMap = setLecture(lectureIds.stream().distinct().collect(Collectors.toList()));
            Map<Long, String> mapCategoryName = mapTopicWithCategoryName(categoryIds);
            if (!CollectionUtils.isEmpty(topicExcelDTOS)) {
                topicDTOS.forEach(t -> topicExcelDTOS.forEach(tx -> {
                    if (Objects.equals(t.getName(), tx.getName())) {
                        if (t.getStatus() != null && t.getStatus()) {
                            tx.setStatus("Đạt");
                        } else {
                            tx.setStatus("Không Đạt");
                        }

                        if (t.getLecturerGuideId() != null && !CollectionUtils.isEmpty(lecturerDTOMap)
                                && lecturerDTOMap.containsKey(t.getLecturerGuideId())) {
                            tx.setLecturerGuideName(lecturerDTOMap.get(t.getLecturerGuideId()).getFullName());
                        }

                        if (t.getLecturerCounterArgumentId() != null && !CollectionUtils.isEmpty(lecturerDTOMap)
                                && lecturerDTOMap.containsKey(t.getLecturerCounterArgumentId())) {
                            tx.setLecturerCounterArgumentName(lecturerDTOMap.get(t.getLecturerCounterArgumentId()).getFullName());
                        }

                        if (t.getCategoryId() != null && !CollectionUtils.isEmpty(mapCategoryName)
                                && mapCategoryName.containsKey(t.getCategoryId())) {
                            tx.setCategoryName(mapCategoryName.get(t.getCategoryId()));
                        }
                    }
                }));
            }

            List<String> headerListNew = Arrays.asList("Tên đề tài", "Chủ đề", "Giáo viên phản biện", "Giáo viên hướng dẫn",
                    "Điểm phản biện", "Điểm hướng dẫn", "Điểm kiểm tra tiến độ lần 1", "Điểm kiểm tra tiến độ lần 2",
                    "Trạng thái", "Số lượng sinh viên", "Năm thực hiện", "Thông tin");

            exportExcel(response, "Export Topic",
                    "Danh sách đồ án", "topic_export", TopicExcelDTO.class.getDeclaredFields(),
                    Arrays.asList(topicExcelDTOS.toArray()), headerListNew);
        }
    }

    private Map<Long, String> mapTopicWithCategoryName(List<Long> categoryIds) {
        Map<Long, String> mapIdCategoryWithCategoryName = new HashMap<>();
        if (categoryIds != null && !categoryIds.isEmpty()) {
            mapIdCategoryWithCategoryName = categoryReps.findByIdIn(categoryIds)
                    .stream().collect(Collectors.toMap(Categories::getId, Categories::getName));
        }

        return mapIdCategoryWithCategoryName;
    }

    public Map<Short, String> getMapStatus() {
        Map<Short, String> mapStatus = new HashMap<>();
        mapStatus.put((short) 1, "ACTIVE");
        mapStatus.put((short) 0, "WAITING");
        mapStatus.put((short) -1, "LOCK");
        return mapStatus;
    }

    public Map<String, String> getTypeAcc() {
        Map<String, String> mapStatus = new HashMap<>();
        mapStatus.put("OTHER", "Quản trị viên");
        mapStatus.put("STUDENT", "Sinh Viên");
        mapStatus.put("LECTURE", "Giảng Viên");
        return mapStatus;
    }

    private byte[] exportExcel(HttpServletResponse response, String sheetName, String title, String fileName,
                                     Field[] fields, List<Object> objects, List<String> headerListNew) throws IOException {
        response.setContentType(type);
        response.setHeader("Content-Disposition", "attachment; filename=" + HashHelper.urlEncode(fileName));

        List<String> headerList = new ArrayList();
        Arrays.stream(fields).forEach(c -> headerList.add(c.getName()));

        ExportExcelUtil ee = new ExportExcelUtil(title, headerListNew, sheetName);

        for (Object categoryDTO : objects) {
            Row row = ee.addRow();

            int i = 0;
            for (String s : headerList) {
                ee.addCell(row, i, getValueSetColumn(s, categoryDTO));
                i += 1;
            }
        }

        ee.write(response.getOutputStream());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.writeTo(response.getOutputStream());
        return byteArrayOutputStream.toByteArray();
    }

    private String getValueSetColumn(String filedName, Object object) {
        if (Boolean.TRUE.equals(Reflections.objectHasProperty(object, filedName))) {
            return Reflections.getFieldValue(object, filedName) != null ?
                    Reflections.getFieldValue(object, filedName).toString() : "";
        }
        return "";
    }
}
