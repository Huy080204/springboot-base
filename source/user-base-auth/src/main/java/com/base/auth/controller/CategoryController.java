package com.base.auth.controller;

import com.base.auth.dto.ApiResponse;
import com.base.auth.dto.ErrorCode;
import com.base.auth.dto.ResponseListDto;
import com.base.auth.form.category.CreateCategoryForm;
import com.base.auth.form.category.UpdateCategoryForm;
import com.base.auth.model.Category;
import com.base.auth.model.criteria.CategoryCriteria;
import com.base.auth.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "/create_category", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> createCategory(@Valid @RequestBody CreateCategoryForm createCategoryForm, BindingResult bindingResult) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        Category category = categoryRepository.findCategoryByName(createCategoryForm.getName());
        if (category != null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.CATEGORY_ERROR_EXIST);
            return apiMessageDto;
        }
        category = new Category();
        category.setName(createCategoryForm.getName());
        category.setDescription(createCategoryForm.getDescription());
        category.setImage(createCategoryForm.getImage());
        category.setOrdering(createCategoryForm.getOrdering());
        category.setKind(createCategoryForm.getKind());
        categoryRepository.save(category);

        apiMessageDto.setMessage("Create category success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update_category", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> updateCategory(@Valid @RequestBody UpdateCategoryForm updateCategoryForm, BindingResult bindingResult) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        Category category = categoryRepository.findById(updateCategoryForm.getCategoryId()).orElse(null);
        if (category == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.CATEGORY_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        category.setName(updateCategoryForm.getName());
        category.setDescription(updateCategoryForm.getDescription());
        category.setImage(updateCategoryForm.getImage());
        category.setOrdering(updateCategoryForm.getOrdering());
        categoryRepository.save(category);

        apiMessageDto.setMessage("Update category success");
        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Category> get(@PathVariable("id") Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        ApiResponse<Category> apiMessageDto = new ApiResponse<>();
        apiMessageDto.setData(category);
        apiMessageDto.setMessage("Get category success");
        return apiMessageDto;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<ResponseListDto<List<Category>>> get(CategoryCriteria criteria, Pageable pageable) {
        Page<Category> pageData = categoryRepository.findAll(criteria.getSpecification(), pageable);

        ResponseListDto<List<Category>> responseListDto = new ResponseListDto<>();
        responseListDto.setContent(pageData.getContent());
        responseListDto.setTotalElements(pageData.getTotalElements());
        responseListDto.setTotalPages(pageData.getTotalPages());

        ApiResponse<ResponseListDto<List<Category>>> apiMessageDto = new ApiResponse<>();
        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get list categories success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> updateCategory(@PathVariable("id") Long id) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.CATEGORY_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        categoryRepository.deleteById(id);

        apiMessageDto.setMessage("Delete category success");
        return apiMessageDto;
    }

}
