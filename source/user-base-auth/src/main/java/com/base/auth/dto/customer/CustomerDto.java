package com.base.auth.dto.customer;

import com.base.auth.dto.account.AccountDto;
import com.base.auth.dto.nation.NationDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerDto {
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "account")
    private AccountDto account;
    @ApiModelProperty(name = "birthDay")
    private LocalDate birthDay;
    @ApiModelProperty(name = "gender")
    private Integer gender;
}
