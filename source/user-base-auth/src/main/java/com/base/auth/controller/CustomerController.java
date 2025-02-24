package com.base.auth.controller;

import com.base.auth.dto.ApiResponse;
import com.base.auth.dto.ErrorCode;
import com.base.auth.dto.ResponseListDto;
import com.base.auth.form.customer.CreateCustomerForm;
import com.base.auth.form.customer.UpdateCustomerForm;
import com.base.auth.mapper.CustomerMapper;
import com.base.auth.model.Account;
import com.base.auth.model.Customer;
import com.base.auth.model.Nation;
import com.base.auth.model.criteria.CustomerCriteria;
import com.base.auth.repository.AccountRepository;
import com.base.auth.repository.CustomerRepository;
import com.base.auth.repository.NationRepository;
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
@RequestMapping("/v1/customer")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CustomerController extends ABasicController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private NationRepository nationRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> createCustomer(@Valid @RequestBody CreateCustomerForm createCustomerForm, BindingResult bindingResult) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        Account account = accountRepository.findById(createCustomerForm.getAccountId()).orElse(null);
        if (account == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_UNKNOWN);
            return apiMessageDto;
        }
        Nation province = nationRepository.findById(createCustomerForm.getProvinceId()).orElse(null);
        if (province == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.NATION_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        Nation district = nationRepository.findById(createCustomerForm.getDistrictId()).orElse(null);
        if (province == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.NATION_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        Nation commune = nationRepository.findById(createCustomerForm.getCommuneId()).orElse(null);
        if (province == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.NATION_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        Customer customer = customerMapper.fromCreateCustomer(createCustomerForm);
        customer.setAccount(account);
        customer.setProvince(province);
        customer.setDistrict(district);
        customer.setCommune(commune);

        customerRepository.save(customer);

        apiMessageDto.setMessage("Create customer success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> updateCustomer(@Valid @RequestBody UpdateCustomerForm updateCustomerForm, BindingResult bindingResult) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        Customer customer = customerRepository.findById(updateCustomerForm.getId()).orElse(null);
        if (customer == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.CUSTOMER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        customerMapper.mappingForUpdateCustomer(updateCustomerForm, customer);

        Nation province = nationRepository.findById(updateCustomerForm.getProvinceId()).orElse(null);
        if (province == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.NATION_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        Nation district = nationRepository.findById(updateCustomerForm.getDistrictId()).orElse(null);
        if (province == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.NATION_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        Nation commune = nationRepository.findById(updateCustomerForm.getCommuneId()).orElse(null);
        if (province == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.NATION_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        customer.setProvince(province);
        customer.setDistrict(district);
        customer.setCommune(commune);
        customerRepository.save(customer);

        apiMessageDto.setMessage("Update customer success");
        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Customer> get(@PathVariable("id") Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        ApiResponse<Customer> apiMessageDto = new ApiResponse<>();
        apiMessageDto.setData(customer);
        apiMessageDto.setMessage("Get customer success");
        return apiMessageDto;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<ResponseListDto<List<Customer>>> get(CustomerCriteria criteria, Pageable pageable) {
        Page<Customer> pageData = customerRepository.findAll(criteria.getSpecification(), pageable);

        ResponseListDto<List<Customer>> responseListDto = new ResponseListDto<>();
        responseListDto.setContent(pageData.getContent());
        responseListDto.setTotalElements(pageData.getTotalElements());
        responseListDto.setTotalPages(pageData.getTotalPages());

        ApiResponse<ResponseListDto<List<Customer>>> apiMessageDto = new ApiResponse<>();
        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get list customers success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> delete(@PathVariable("id") Long id) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.CUSTOMER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        customerRepository.deleteById(id);

        apiMessageDto.setMessage("Delete customer success");
        return apiMessageDto;
    }

}
