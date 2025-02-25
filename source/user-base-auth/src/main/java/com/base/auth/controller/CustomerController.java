package com.base.auth.controller;

import com.base.auth.constant.UserBaseConstant;
import com.base.auth.dto.ApiMessageDto;
import com.base.auth.dto.ApiResponse;
import com.base.auth.dto.ErrorCode;
import com.base.auth.dto.ResponseListDto;
import com.base.auth.dto.customer.CustomerDto;
import com.base.auth.form.customer.CreateCustomerForm;
import com.base.auth.form.customer.UpdateCustomerForm;
import com.base.auth.mapper.CustomerMapper;
import com.base.auth.model.Account;
import com.base.auth.model.Customer;
import com.base.auth.model.Group;
import com.base.auth.model.Nation;
import com.base.auth.model.criteria.CustomerCriteria;
import com.base.auth.repository.AccountRepository;
import com.base.auth.repository.CustomerRepository;
import com.base.auth.repository.GroupRepository;
import com.base.auth.repository.NationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    private GroupRepository groupRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CUS_C')")
    public ApiResponse<String> createCustomer(@Valid @RequestBody CreateCustomerForm createCustomerForm, BindingResult bindingResult) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();

        // check username exist
        Account account = accountRepository.findAccountByUsername(createCustomerForm.getUsername());
        if (account != null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_USERNAME_EXIST);
            return apiMessageDto;
        }

        // get group kind user
        Group group = groupRepository.findFirstByKind(UserBaseConstant.GROUP_KIND_USER);
        if (group == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.ACCOUNT_ERROR_UNKNOWN);
            return apiMessageDto;
        }

        // create account
        account = new Account();
        account.setUsername(createCustomerForm.getUsername());
        account.setPassword(passwordEncoder.encode(createCustomerForm.getPassword()));
        account.setFullName(createCustomerForm.getFullName());
        account.setKind(UserBaseConstant.USER_KIND_USER);
        account.setEmail(createCustomerForm.getEmail());
        account.setAvatarPath(createCustomerForm.getAvatarPath());
        account.setGroup(group);
        account.setPhone(createCustomerForm.getPhone());

        // get province
        Nation province = nationRepository.findByIdAndType(createCustomerForm.getProvinceId(), UserBaseConstant.NATION_KIND_PROVINCE)
                .orElse(null);
        if (province == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.NATION_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        // get district
        Nation district = nationRepository.findByIdAndType(createCustomerForm.getProvinceId(), UserBaseConstant.NATION_KIND_DISTRICT)
                .orElse(null);
        if (district == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.NATION_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        // get commune
        Nation commune = nationRepository.findByIdAndType(createCustomerForm.getProvinceId(), UserBaseConstant.NATION_KIND_COMMUNE)
                .orElse(null);
        if (commune == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.NATION_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        // create customer
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
    @PreAuthorize("hasRole('CUS_U')")
    public ApiResponse<String> updateCustomer(@Valid @RequestBody UpdateCustomerForm updateCustomerForm, BindingResult bindingResult) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        Customer customer = customerRepository.findById(updateCustomerForm.getId()).orElse(null);
        if (customer == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.CUSTOMER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        customerMapper.mappingForUpdateCustomer(updateCustomerForm, customer);
        customer.getAccount().setEmail(updateCustomerForm.getEmail());
        customer.getAccount().setPhone(updateCustomerForm.getPhone());
        customer.getAccount().setPassword(passwordEncoder.encode(updateCustomerForm.getPassword()));
        customer.getAccount().setFullName(updateCustomerForm.getFullName());
        customer.getAccount().setAvatarPath(updateCustomerForm.getAvatarPath());

        Nation province = nationRepository.findByIdAndType(updateCustomerForm.getProvinceId(), UserBaseConstant.NATION_KIND_PROVINCE)
                .orElse(null);
        if (province == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.NATION_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        Nation district = nationRepository.findByIdAndType(updateCustomerForm.getProvinceId(), UserBaseConstant.NATION_KIND_DISTRICT)
                .orElse(null);
        if (district == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.NATION_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        Nation commune = nationRepository.findByIdAndType(updateCustomerForm.getProvinceId(), UserBaseConstant.NATION_KIND_COMMUNE)
                .orElse(null);
        if (commune == null) {
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
    @PreAuthorize("hasRole('CUS_V')")
    public ApiResponse<CustomerDto> get(@PathVariable("id") Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        CustomerDto customerDto = customerMapper.fromCustomerToDto(customer);
        ApiResponse<CustomerDto> apiMessageDto = new ApiResponse<>();
        apiMessageDto.setData(customerDto);
        apiMessageDto.setMessage("Get customer success");
        return apiMessageDto;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CUS_L')")
    public ApiMessageDto<ResponseListDto<List<CustomerDto>>> get(CustomerCriteria criteria, Pageable pageable) {
        Page<Customer> pageData = customerRepository.findAll(criteria.getSpecification(), pageable);

        ResponseListDto<List<CustomerDto>> responseListDto = new ResponseListDto<>();
        responseListDto.setContent(
                pageData.getContent().stream()
                        .map(customerMapper::fromCustomerToDto)
                        .collect(Collectors.toList())

        );
        responseListDto.setTotalElements(pageData.getTotalElements());
        responseListDto.setTotalPages(pageData.getTotalPages());

        ApiMessageDto<ResponseListDto<List<CustomerDto>>> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get list customers success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CUS_D')")
    public ApiResponse<String> delete(@PathVariable("id") Long id) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.CUSTOMER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        accountRepository.deleteById(customer.getAccount().getId());

        customerRepository.deleteById(id);

        apiMessageDto.setMessage("Delete customer success");
        return apiMessageDto;
    }

}
