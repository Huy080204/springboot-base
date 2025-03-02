package com.base.auth.form.customer;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@ApiModel
public class UpdateCustomerForm {

    @NotNull(message = "id cant not be null")
    @ApiModelProperty(name = "id", required = true)
    private Long id;

    @ApiModelProperty(name = "email")
    private String email;

    @ApiModelProperty(name = "phone")
    private String phone;

    @ApiModelProperty(name = "password")
    private String password;

    @NotEmpty(message = "fullName cant not be null")
    @ApiModelProperty(name = "fullName", required = true)
    private String fullName;

    @ApiModelProperty(name = "avatarPath")
    private String avatarPath;

    @ApiModelProperty(name = "birthDay", required = false)
    private LocalDate birthDay;

    @ApiModelProperty(name = "gender", required = false)
    private Integer gender;

}
