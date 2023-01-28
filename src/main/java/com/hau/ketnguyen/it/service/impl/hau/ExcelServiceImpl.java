package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.common.anotations.Reflections;
import com.hau.ketnguyen.it.common.util.ExportExcelUtil;
import com.hau.ketnguyen.it.model.dto.auth.UserDTO;
import com.hau.ketnguyen.it.model.dto.excel.CategoryExcelDTO;
import com.hau.ketnguyen.it.model.dto.excel.FacultyExcelDTO;
import com.hau.ketnguyen.it.model.dto.excel.UserExcelDTO;
import com.hau.ketnguyen.it.model.dto.excel.WorkplaceExcelDTO;
import com.hau.ketnguyen.it.model.dto.hau.CategoryDTO;
import com.hau.ketnguyen.it.model.dto.hau.FacultyDTO;
import com.hau.ketnguyen.it.model.dto.hau.WorkplaceDTO;
import com.hau.ketnguyen.it.model.request.auth.UserRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchCategoryRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchFacultyRequest;
import com.hau.ketnguyen.it.model.request.hau.SearchWorkplaceRequest;
import com.hau.ketnguyen.it.repository.auth.UserReps;
import com.hau.ketnguyen.it.repository.hau.CategoryReps;
import com.hau.ketnguyen.it.repository.hau.FacultyReps;
import com.hau.ketnguyen.it.repository.hau.WorkplaceReps;
import com.hau.ketnguyen.it.service.*;
import com.hau.ketnguyen.it.service.mapper.CategoryMapper;
import com.hau.ketnguyen.it.service.mapper.FacultyMapper;
import com.hau.ketnguyen.it.service.mapper.UserMapper;
import com.hau.ketnguyen.it.service.mapper.WorkplaceMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ExcelServiceImpl implements ExcelService {
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

    @Override
    public void exportCategory(SearchCategoryRequest request, HttpServletResponse response) throws Exception {
        long count = categoryReps.count();
        request.setSize((int) count);
        List<CategoryDTO> categoryDTOS = categoryService.getAll(request).getData()
                .stream().sorted(Comparator.comparingLong(CategoryDTO::getId)).collect(Collectors.toList());
        List<CategoryExcelDTO> categoryExcelDTOS = categoryMapper.toExcel(categoryDTOS);
        List<String> headerListNew = Arrays.asList("Mã chủ đề", "Tên chủ đề");

        exportExcel(response, "Export Category",
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
                        if (!CollectionUtils.isEmpty(workplaceDTOMap) && workplaceDTOMap.containsKey(f.getWorkplaceId())) {
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

    private void exportExcel(HttpServletResponse response, String sheetName, String title, String fileName,
                             Field[] fields, List<Object> objects, List<String> headerListNew) throws IOException {
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

        ee.write(response, fileName.concat(".xlsx"));
        ee.dispose();

    }

    private String getValueSetColumn(String filedName, Object object) {
        if (Boolean.TRUE.equals(Reflections.objectHasProperty(object, filedName))) {
            return Reflections.getFieldValue(object, filedName) != null ?
                    Reflections.getFieldValue(object, filedName).toString() : "";
        }
        return "";
    }
}
