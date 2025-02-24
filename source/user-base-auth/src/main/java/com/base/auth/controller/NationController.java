package com.base.auth.controller;

import com.base.auth.dto.ApiResponse;
import com.base.auth.dto.ErrorCode;
import com.base.auth.dto.ResponseListDto;
import com.base.auth.form.customer.CreateCustomerForm;
import com.base.auth.form.customer.UpdateCustomerForm;
import com.base.auth.form.nation.CreateNationForm;
import com.base.auth.form.nation.UpdateNationForm;
import com.base.auth.mapper.CustomerMapper;
import com.base.auth.mapper.NationMapper;
import com.base.auth.model.Account;
import com.base.auth.model.Customer;
import com.base.auth.model.Nation;
import com.base.auth.model.criteria.CustomerCriteria;
import com.base.auth.model.criteria.NationCriteria;
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
@RequestMapping("/v1/nation")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class NationController extends ABasicController {

    @Autowired
    private NationRepository nationRepository;

    private NationMapper nationMapper;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> create(@Valid @RequestBody CreateNationForm createNationForm, BindingResult bindingResult) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();

        Nation nation = nationMapper.fromCreateNation(createNationForm);

        nationRepository.save(nation);

        apiMessageDto.setMessage("Create nation success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> update(@Valid @RequestBody UpdateNationForm updateNationForm, BindingResult bindingResult) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        Nation nation = nationRepository.findById(updateNationForm.getId()).orElse(null);
        if (nation == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.NATION_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        nationMapper.mappingForUpdateNation(updateNationForm, nation);

        apiMessageDto.setMessage("Update nation success");
        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Nation> get(@PathVariable("id") Long id) {
        Nation nation = nationRepository.findById(id).orElse(null);
        ApiResponse<Nation> apiMessageDto = new ApiResponse<>();
        apiMessageDto.setData(nation);
        apiMessageDto.setMessage("Get nation success");
        return apiMessageDto;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<ResponseListDto<List<Nation>>> get(NationCriteria criteria, Pageable pageable) {
        Page<Nation> pageData = nationRepository.findAll(criteria.getSpecification(), pageable);

        ResponseListDto<List<Nation>> responseListDto = new ResponseListDto<>();
        responseListDto.setContent(pageData.getContent());
        responseListDto.setTotalElements(pageData.getTotalElements());
        responseListDto.setTotalPages(pageData.getTotalPages());

        ApiResponse<ResponseListDto<List<Nation>>> apiMessageDto = new ApiResponse<>();
        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get list nations success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> delete(@PathVariable("id") Long id) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        Nation nation = nationRepository.findById(id).orElse(null);
        if (nation == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.NATION_ERROR_NOT_FOUND);
            return apiMessageDto;
        }
        nationRepository.deleteById(id);

        apiMessageDto.setMessage("Delete nation success");
        return apiMessageDto;
    }

}
