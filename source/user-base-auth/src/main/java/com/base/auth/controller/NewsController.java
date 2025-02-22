package com.base.auth.controller;

import com.base.auth.dto.ApiResponse;
import com.base.auth.dto.ErrorCode;
import com.base.auth.dto.ResponseListDto;
import com.base.auth.form.news.CreateNewsForm;
import com.base.auth.form.news.UpdateNewsForm;
import com.base.auth.model.Category;
import com.base.auth.model.News;
import com.base.auth.model.criteria.NewsCriteria;
import com.base.auth.repository.CategoryRepository;
import com.base.auth.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/news")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NewsController {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping(value = "/create_news", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> createNews(@Valid @RequestBody CreateNewsForm createNewsForm, BindingResult bindingResult) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();

        News news = newsRepository.findNewsByTitle(createNewsForm.getTitle());
        if (news != null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.NEWS_ERROR_EXISTED);
            return apiMessageDto;
        }

        Category category = categoryRepository.findById(createNewsForm.getCategoryId()).orElse(null);
        if (category == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.CATEGORY_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        news = new News();
        news.setTitle(createNewsForm.getTitle());
        news.setContent(createNewsForm.getContent());
        news.setAvatar(createNewsForm.getAvatar());
        news.setBanner(createNewsForm.getBanner());
        news.setDescription(createNewsForm.getDescription());
        news.setCategory(category);

        newsRepository.save(news);

        apiMessageDto.setMessage("Create category success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update_news", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> updateCategory(@Valid @RequestBody UpdateNewsForm updateNewsForm, BindingResult bindingResult) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();

        News news = newsRepository.findById(updateNewsForm.getId()).orElse(null);
        if (news == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.NEWS_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        Category category = categoryRepository.findById(updateNewsForm.getCategoryId()).orElse(null);
        if (category == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.CATEGORY_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        news.setTitle(updateNewsForm.getTitle());
        news.setContent(updateNewsForm.getContent());
        news.setAvatar(updateNewsForm.getAvatar());
        news.setBanner(updateNewsForm.getBanner());
        news.setDescription(updateNewsForm.getDescription());
        news.setStatus(updateNewsForm.getStatus());
        news.setPinTop(updateNewsForm.getPinTop());
        news.setCategory(category);

        newsRepository.save(news);

        apiMessageDto.setMessage("Update news success");
        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<News> get(@PathVariable("id") Long id) {
        News news = newsRepository.findById(id).orElse(null);
        ApiResponse<News> apiMessageDto = new ApiResponse<>();
        apiMessageDto.setData(news);
        apiMessageDto.setMessage("Get news success");
        return apiMessageDto;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<ResponseListDto<List<News>>> get(NewsCriteria criteria, Pageable pageable) {
        Page<News> pageData = newsRepository.findAll(criteria.getSpecification(), pageable);

        ResponseListDto<List<News>> responseListDto = new ResponseListDto<>();
        responseListDto.setContent(pageData.getContent());
        responseListDto.setTotalElements(pageData.getTotalElements());
        responseListDto.setTotalPages(pageData.getTotalPages());

        ApiResponse<ResponseListDto<List<News>>> apiMessageDto = new ApiResponse<>();
        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get list news success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> updateCategory(@PathVariable("id") Long id) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        News news = newsRepository.findById(id).orElse(null);
        if (news == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.NEWS_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        newsRepository.deleteById(id);

        apiMessageDto.setMessage("Delete news success");
        return apiMessageDto;
    }
}
