package com.base.auth.controller;

import com.base.auth.dto.ApiMessageDto;
import com.base.auth.dto.ApiResponse;
import com.base.auth.dto.ErrorCode;
import com.base.auth.dto.ResponseListDto;
import com.base.auth.dto.product.ProductDto;
import com.base.auth.form.product.CreateProductForm;
import com.base.auth.form.product.UpdateProductForm;
import com.base.auth.mapper.ProductMapper;
import com.base.auth.model.*;
import com.base.auth.model.criteria.ProductCriteria;
import com.base.auth.repository.*;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/product")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ProductController extends ABasicController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PRO_C')")
    public ApiResponse<String> create(@Valid @RequestBody CreateProductForm createProductForm, BindingResult bindingResult) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();

        // check name exist
        Product product = productRepository.findByName(createProductForm.getName());
        if (product != null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.PRODUCT_ERROR_EXIST);
            return apiMessageDto;
        }

        // create product
        product = productMapper.fromCreateProductFormToEntity(createProductForm);

        productRepository.save(product);

        apiMessageDto.setMessage("Create product success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PRO_U')")
    public ApiResponse<String> update(@Valid @RequestBody UpdateProductForm updateProductForm, BindingResult bindingResult) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        Product product = productRepository.findById(updateProductForm.getId()).orElse(null);
        if (product == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.PRODUCT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        if (!product.getName().equals(updateProductForm.getName())) {
            if (productRepository.existsByName(updateProductForm.getName())) {
                apiMessageDto.setResult(false);
                apiMessageDto.setCode(ErrorCode.PRODUCT_ERROR_EXIST);
                return apiMessageDto;
            }
        }
        productMapper.updateProductFromUpdateProductForm(updateProductForm, product);

        productRepository.save(product);

        apiMessageDto.setMessage("Update product success");
        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PRO_V')")
    public ApiResponse<ProductDto> get(@PathVariable("id") Long id) {
        Product product = productRepository.findById(id).orElse(null);
        ProductDto productDto = productMapper.fromEntityToDto(product);
        ApiResponse<ProductDto> apiMessageDto = new ApiResponse<>();
        apiMessageDto.setData(productDto);
        apiMessageDto.setMessage("Get product success");
        return apiMessageDto;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PRO_L')")
    public ApiMessageDto<ResponseListDto<List<ProductDto>>> get(ProductCriteria criteria, Pageable pageable) {
        Page<Product> pageData = productRepository.findAll(criteria.getSpecification(), pageable);

        ResponseListDto<List<ProductDto>> responseListDto = new ResponseListDto<>();
        responseListDto.setContent(productMapper.fromEntityToDtoList(pageData.getContent()));
        responseListDto.setTotalElements(pageData.getTotalElements());
        responseListDto.setTotalPages(pageData.getTotalPages());

        ApiMessageDto<ResponseListDto<List<ProductDto>>> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get list products success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PRO_D')")
    public ApiResponse<String> delete(@PathVariable("id") Long id) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.PRODUCT_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        productRepository.deleteById(id);

        apiMessageDto.setMessage("Delete product success");
        return apiMessageDto;
    }

}
