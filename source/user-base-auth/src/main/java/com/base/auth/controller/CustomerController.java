package com.base.auth.controller;

import com.base.auth.constant.UserBaseConstant;
import com.base.auth.dto.ApiMessageDto;
import com.base.auth.dto.ErrorCode;
import com.base.auth.dto.ResponseListDto;
import com.base.auth.dto.customer.CustomerDto;
import com.base.auth.dto.customerAddress.CustomerAddressDto;
import com.base.auth.form.customer.CreateCustomerForm;
import com.base.auth.form.customer.UpdateCustomerForm;
import com.base.auth.form.customerAddress.CreateCustomerAddressForm;
import com.base.auth.form.customerAddress.UpdateCustomerAddressForm;
import com.base.auth.mapper.CustomerAddressMapper;
import com.base.auth.mapper.CustomerMapper;
import com.base.auth.model.*;
import com.base.auth.model.criteria.CustomerAddressCriteria;
import com.base.auth.model.criteria.CustomerCriteria;
import com.base.auth.repository.*;
import com.base.auth.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
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
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerAddressRepository customerAddressRepository;

    @Autowired
    private CustomerAddressMapper customerAddressMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasRole('CUS_C')")
    public ApiMessageDto<String> createCustomer(@Valid @RequestBody CreateCustomerForm createCustomerForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

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

        // create customer
        Customer customer = customerMapper.fromCreateCustomer(createCustomerForm);

        customer.setAccount(account);

        customerRepository.save(customer);

        // create cart
        Cart cart = new Cart();
        cart.setCode(StringUtils.generateUniqueCode(6, cartRepository));
        cart.setCustomer(customer);
        cartRepository.save(cart);

        apiMessageDto.setMessage("Create customer success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CUS_U')")
    public ApiMessageDto<String> updateCustomer(@Valid @RequestBody UpdateCustomerForm updateCustomerForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        // check customer exists
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

        customerRepository.save(customer);

        apiMessageDto.setMessage("Update customer success");
        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CUS_V')")
    public ApiMessageDto<CustomerDto> get(@PathVariable("id") Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        CustomerDto customerDto = customerMapper.fromCustomerToDto(customer);
        ApiMessageDto<CustomerDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(customerDto);
        apiMessageDto.setMessage("Get customer success");
        return apiMessageDto;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasRole('CUS_L')")
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
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.CUSTOMER_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        Cart cart = cartRepository.findByCustomerId(customer.getId()).orElse(null);
        if (cart != null) {
            cartItemRepository.deleteAll(cart.getCartItems());
            cartRepository.delete(cart);
        }

        List<Order> orders = orderRepository.findByCustomerId(id);
        for (Order order : orders) {
            orderItemRepository.deleteAll(order.getOrderItems());
            orderRepository.delete(order);
        }

        if (customer.getAccount() != null) {
            accountRepository.deleteById(customer.getAccount().getId());
        }

        customerRepository.deleteById(id);

        apiMessageDto.setMessage("Delete customer success");
        return apiMessageDto;
    }

    @PostMapping(value = "/create-address", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasRole('CUS_ADR_C')")
    public ApiMessageDto<String> createCustomerAddress(@Valid @RequestBody CreateCustomerAddressForm createCustomerAddressForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        long customerId = getCurrentUser();

        // check customer exists
        Customer customer = customerRepository.findById(customerId).orElse(null);

        if (customer == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.CUSTOMER_ERROR_NOT_FOUND);
            apiMessageDto.setMessage("Customer not found");
            return apiMessageDto;
        }

        // get province
        Nation province = nationRepository.findByIdAndType(createCustomerAddressForm.getProvinceId(), UserBaseConstant.NATION_KIND_PROVINCE)
                .orElse(null);
        if (province == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.NATION_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        // get district
        Nation district = nationRepository.findByIdAndType(createCustomerAddressForm.getDistrictId(), UserBaseConstant.NATION_KIND_DISTRICT)
                .orElse(null);
        if (district == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.NATION_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        // get commune
        Nation commune = nationRepository.findByIdAndType(createCustomerAddressForm.getCommuneId(), UserBaseConstant.NATION_KIND_COMMUNE)
                .orElse(null);
        if (commune == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.NATION_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        CustomerAddress customerAddress = customerAddressMapper.fromCreateCustomerAddress(createCustomerAddressForm);
        customerAddress.setCustomer(customer);
        customerAddress.setProvince(province);
        customerAddress.setDistrict(district);
        customerAddress.setCommune(commune);


        if (customerAddress.isDefaultAddress()) {
            // error resetDefaultAddress
            customerAddressRepository.resetDefaultAddress(customerId);
        }

        customerAddressRepository.save(customerAddress);

        apiMessageDto.setMessage("Create customer address success");
        return apiMessageDto;
    }

    @PostMapping(value = "/update-address", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CUS_ADR_U')")
    public ApiMessageDto<String> updateCustomerAddress(@Valid @RequestBody UpdateCustomerAddressForm updateCustomerAddressForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        CustomerAddress customerAddress = customerAddressRepository.findById(updateCustomerAddressForm.getCustomerAddressId())
                .orElse(null);

        if (customerAddress == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.CUSTOMER_ADDRESS_ERROR_NOT_FOUND);
            apiMessageDto.setMessage("Customer address not found");
            return apiMessageDto;
        }

        customerAddressMapper.mappingForUpdateCustomerAddress(updateCustomerAddressForm, customerAddress);

        // update province
        if (!updateCustomerAddressForm.getProvinceId().equals(customerAddress.getProvince().getId())) {
            Nation province = nationRepository.findByIdAndType(updateCustomerAddressForm.getProvinceId(), UserBaseConstant.NATION_KIND_PROVINCE)
                    .orElse(null);
            if (province == null) {
                apiMessageDto.setResult(false);
                apiMessageDto.setCode(ErrorCode.NATION_ERROR_NOT_FOUND);
                return apiMessageDto;
            }
            customerAddress.setProvince(province);
        }

        // update district
        if (!updateCustomerAddressForm.getDistrictId().equals(customerAddress.getDistrict().getId())) {
            Nation district = nationRepository.findByIdAndType(updateCustomerAddressForm.getDistrictId(), UserBaseConstant.NATION_KIND_DISTRICT)
                    .orElse(null);
            if (district == null) {
                apiMessageDto.setResult(false);
                apiMessageDto.setCode(ErrorCode.NATION_ERROR_NOT_FOUND);
                return apiMessageDto;
            }
            customerAddress.setDistrict(district);
        }

        // get commune
        if (!updateCustomerAddressForm.getCommuneId().equals(customerAddress.getCommune().getId())) {
            Nation commune = nationRepository.findByIdAndType(updateCustomerAddressForm.getCommuneId(), UserBaseConstant.NATION_KIND_COMMUNE)
                    .orElse(null);
            if (commune == null) {
                apiMessageDto.setResult(false);
                apiMessageDto.setCode(ErrorCode.NATION_ERROR_NOT_FOUND);
                return apiMessageDto;
            }
            customerAddress.setCommune(commune);
        }

        if (updateCustomerAddressForm.getDefaultAddress()) {
            customerAddressRepository.resetDefaultAddress(customerAddress.getCustomer().getId());
        }

        customerAddressRepository.save(customerAddress);

        apiMessageDto.setMessage("Update customer address success");
        return apiMessageDto;
    }

    @GetMapping(value = "/list-address", produces = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasRole('CUS_ADR_L')")
    public ApiMessageDto<ResponseListDto<List<CustomerAddressDto>>> getCustomerAddressList(
            @Valid @ModelAttribute CustomerAddressCriteria customerAddressCriteria,
            Pageable pageable
    ) {
        Specification<CustomerAddress> specification = customerAddressCriteria.getSpecification();
        Page<CustomerAddress> customerAddressPage = customerAddressRepository.findAll(specification, pageable);

        ResponseListDto<List<CustomerAddressDto>> result = new ResponseListDto<>(
                customerAddressMapper.fromEntityToCustomerAddressDtoList(customerAddressPage.getContent()),
                customerAddressPage.getTotalElements(),
                customerAddressPage.getTotalPages()
        );
        ApiMessageDto<ResponseListDto<List<CustomerAddressDto>>> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(result);
        apiMessageDto.setMessage("Get customer address list successfully");

        return apiMessageDto;
    }

    @GetMapping(value = "/get-address/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CUS_ADR_V')")
    public ApiMessageDto<CustomerAddressDto> getCustomerAddressById(@PathVariable Long id) {
        ApiMessageDto<CustomerAddressDto> apiMessageDto = new ApiMessageDto<>();

        CustomerAddress customerAddress = customerAddressRepository.findById(id).orElseThrow(null);
        if (customerAddress == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.CUSTOMER_ADDRESS_ERROR_NOT_FOUND);
            apiMessageDto.setMessage("Customer address not found");
            return apiMessageDto;
        }
        apiMessageDto.setData(customerAddressMapper.fromEntityToCustomerAddressDto(customerAddress));
        apiMessageDto.setMessage("Get customer address successfully");

        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete-address/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CUS_ADR_D')")
    public ApiMessageDto<String> deleteCustomerAddress(@PathVariable Long id) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        CustomerAddress customerAddress = customerAddressRepository.findById(id).orElseThrow(null);
        if (customerAddress == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.CUSTOMER_ADDRESS_ERROR_NOT_FOUND);
            apiMessageDto.setMessage("Customer address not found");
            return apiMessageDto;
        }
        customerAddressRepository.deleteById(id);
        apiMessageDto.setMessage("Delete customer address successfully");

        return apiMessageDto;
    }

}
