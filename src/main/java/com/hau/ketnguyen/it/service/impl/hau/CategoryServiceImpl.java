package com.hau.ketnguyen.it.service.impl.hau;

import com.hau.ketnguyen.it.common.exception.APIException;
import com.hau.ketnguyen.it.common.util.BeanUtil;
import com.hau.ketnguyen.it.common.util.PageableUtils;
import com.hau.ketnguyen.it.entity.hau.Categories;
import com.hau.ketnguyen.it.model.dto.hau.CategoryDTO;
import com.hau.ketnguyen.it.model.request.hau.SearchCategoryRequest;
import com.hau.ketnguyen.it.model.response.PageDataResponse;
import com.hau.ketnguyen.it.repository.hau.CategoryReps;
import com.hau.ketnguyen.it.service.CategoryService;
import com.hau.ketnguyen.it.service.mapper.CategoryMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryReps categoryReps;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDTO save(CategoryDTO categoryDTO) {
        return categoryMapper.to(categoryReps.save(categoryMapper.from(categoryDTO)));
    }

    @Override
    public CategoryDTO edit(Long id, CategoryDTO categoryDTO) {
        Optional<Categories> categoriesOptional = categoryReps.findById(id);

        if (categoriesOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy chủ đề");
        }

        Categories categories = categoriesOptional.get();
        BeanUtil.copyNonNullProperties(categoryDTO, categories);

        return categoryMapper.to(categoryReps.save(categories));
    }

    @Override
    public void delete(Long id) {
        Optional<Categories> categoriesOptional = categoryReps.findById(id);

        if (categoriesOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy chủ đề");
        }

        categoryReps.delete(categoriesOptional.get());
    }

    @Override
    public CategoryDTO findById(Long id) {
        Optional<Categories> categoriesOptional = categoryReps.findById(id);

        if (categoriesOptional.isEmpty()) {
            throw APIException.from(HttpStatus.NOT_FOUND).withMessage("Không tìm thấy chủ đề");
        }

        return categoryMapper.to(categoriesOptional.get());
    }

    @Override
    public PageDataResponse<CategoryDTO> getAll(SearchCategoryRequest request) {
        setCategoryRequest(request);
        Pageable pageable = PageableUtils.of(request.getPage(), request.getSize());
        Page<CategoryDTO> categoryDTOPage = categoryReps.search(request, pageable).map(categoryMapper::to);
        return PageDataResponse.of(categoryDTOPage);
    }

    private void setCategoryRequest(SearchCategoryRequest request) {
        if (request.getName() != null) {
            request.setName(request.getName().toLowerCase());
        }

        if (request.getCode() != null) {
            request.setCode(request.getCode().toLowerCase());
        }
    }
}
