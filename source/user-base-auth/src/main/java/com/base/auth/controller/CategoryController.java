package com.base.auth.controller;

import com.base.auth.dto.ApiResponse;
import com.base.auth.dto.ErrorCode;
import com.base.auth.dto.ResponseListDto;
import com.base.auth.dto.category.CategoryDto;
import com.base.auth.form.category.CreateCategoryForm;
import com.base.auth.form.category.UpdateCategoryForm;
import com.base.auth.mapper.CategoryMapper;
import com.base.auth.model.Category;
import com.base.auth.model.criteria.CategoryCriteria;
import com.base.auth.repository.CategoryRepository;
import com.base.auth.repository.NewsRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/category")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CategoryController extends ABasicController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CATE_C')")
    public ApiResponse<String> createCategory(@Valid @RequestBody CreateCategoryForm createCategoryForm, BindingResult bindingResult) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        Category category = categoryRepository.findCategoryByName(createCategoryForm.getName());
        if (category != null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.CATEGORY_ERROR_EXIST);
            return apiMessageDto;
        }
        category = categoryMapper.fromCreateCategory(createCategoryForm);
        categoryRepository.save(category);

        apiMessageDto.setMessage("Create category success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CATE_U')")
    public ApiResponse<String> updateCategory(@Valid @RequestBody UpdateCategoryForm updateCategoryForm, BindingResult bindingResult) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        Category category = categoryRepository.findById(updateCategoryForm.getCategoryId()).orElse(null);
        if (category == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.CATEGORY_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        categoryMapper.mappingForUpdateServiceCategory(updateCategoryForm, category);
        categoryRepository.save(category);

        apiMessageDto.setMessage("Update category success");
        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CATE_V')")
    public ApiResponse<CategoryDto> get(@PathVariable("id") Long id) {
        ApiResponse<CategoryDto> apiMessageDto = new ApiResponse<>();

        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.CATEGORY_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        CategoryDto categoryDto = categoryMapper.fromEntityToCategoryDto(category);
        apiMessageDto.setData(categoryDto);
        apiMessageDto.setMessage("Get category success");
        return apiMessageDto;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CATE_L')")
    public ApiResponse<ResponseListDto<List<CategoryDto>>> get(CategoryCriteria criteria, Pageable pageable) {
        Page<Category> pageData = categoryRepository.findAll(criteria.getSpecification(), pageable);

        ResponseListDto<List<CategoryDto>> responseListDto = new ResponseListDto<>();
        responseListDto.setContent(categoryMapper.fromEntityToDtoList(pageData.getContent()));
        responseListDto.setTotalElements(pageData.getTotalElements());
        responseListDto.setTotalPages(pageData.getTotalPages());

        ApiResponse<ResponseListDto<List<CategoryDto>>> apiMessageDto = new ApiResponse<>();
        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get list categories success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CATE_D')")
    public ApiResponse<String> delete(@PathVariable("id") Long id) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.CATEGORY_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        if (newsRepository.countNewsByCategoryId(category.getId()) > 0) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.CATEGORY_ERROR_CANT_DELETE_RELATIONSHIP_WITH_NEWS);
            return apiMessageDto;
        }

        categoryRepository.deleteById(id);

        apiMessageDto.setMessage("Delete category success");
        return apiMessageDto;
    }

}
