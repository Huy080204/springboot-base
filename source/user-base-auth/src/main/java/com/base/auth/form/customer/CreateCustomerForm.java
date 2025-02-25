package com.base.auth.form.customer;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@ApiModel
public class CreateCustomerForm {
    @NotEmpty(message = "username cant not be null")
    @ApiModelProperty(name = "username", required = true)
    private String username;

    @ApiModelProperty(name = "email")
    @Email
    private String email;

    @ApiModelProperty(name = "phone")
    private String phone;

    @NotEmpty(message = "password cant not be null")
    @ApiModelProperty(name = "password", required = true)
    private String password;

    @NotEmpty(message = "fullName cant not be null")
    @ApiModelProperty(name = "fullName",example = "Tam Nguyen", required = true)
    private String fullName;

    @ApiModelProperty(name = "avatarPath", required = false)
    private String avatarPath;

    @ApiModelProperty(name = "birthDay", required = false)
    private LocalDate birthDay;

    @ApiModelProperty(name = "gender", required = false)
    private Integer gender;

    @ApiModelProperty(name = "address", required = false)
    private String address;

    @ApiModelProperty(name = "provinceId", required = false)
    private Long provinceId;

    @ApiModelProperty(name = "districtId", required = false)
    private Long districtId;

    @ApiModelProperty(name = "communeId", required = false)
    private Long communeId;

}
