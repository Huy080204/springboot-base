package com.base.auth.controller;

import com.base.auth.dto.ApiResponse;
import com.base.auth.dto.ErrorCode;
import com.base.auth.dto.ResponseListDto;
import com.base.auth.dto.nation.NationDto;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/nation")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class NationController extends ABasicController {

    @Autowired
    private NationRepository nationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private NationMapper nationMapper;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NAT_C')")
    public ApiResponse<String> create(@Valid @RequestBody CreateNationForm createNationForm, BindingResult bindingResult) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();

        Nation nation = nationMapper.fromCreateNation(createNationForm);

        nationRepository.save(nation);

        apiMessageDto.setMessage("Create nation success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NAT_U')")
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
    @PreAuthorize("hasRole('NAT_V')")
    public ApiResponse<NationDto> get(@PathVariable("id") Long id) {
        Nation nation = nationRepository.findById(id).orElse(null);
        NationDto nationDto = nationMapper.fromNationToDto(nation);
        ApiResponse<NationDto> apiMessageDto = new ApiResponse<>();
        apiMessageDto.setData(nationDto);
        apiMessageDto.setMessage("Get nation success");
        return apiMessageDto;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NAT_L')")
    public ApiResponse<ResponseListDto<List<NationDto>>> get(NationCriteria criteria, Pageable pageable) {
        Page<Nation> pageData = nationRepository.findAll(criteria.getSpecification(), pageable);

        ResponseListDto<List<NationDto>> responseListDto = new ResponseListDto<>();
        responseListDto.setContent(
                pageData.getContent().stream()
                        .map(nationMapper::fromNationToDto)
                        .collect(Collectors.toList())
        );
        responseListDto.setTotalElements(pageData.getTotalElements());
        responseListDto.setTotalPages(pageData.getTotalPages());

        ApiResponse<ResponseListDto<List<NationDto>>> apiMessageDto = new ApiResponse<>();
        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("Get list nations success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NAT_D')")
    public ApiResponse<String> delete(@PathVariable("id") Long id) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        Nation nation = nationRepository.findById(id).orElse(null);
        if (nation == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.NATION_ERROR_NOT_FOUND);
            return apiMessageDto;
        }

        if (customerRepository.countCustomerByNationId(id) > 0) {
            apiMessageDto.setResult(false);
            apiMessageDto.setCode(ErrorCode.NATION_ERROR_CANT_DELETE_RELATIONSHIP_WITH_ADDRESS);
            return apiMessageDto;
        }

        nationRepository.deleteById(id);

        apiMessageDto.setMessage("Delete nation success");
        return apiMessageDto;
    }

}
