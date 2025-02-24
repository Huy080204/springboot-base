package com.base.auth.form.customer;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@ApiModel
public class CreateCustomerForm {
    @NotNull(message = "accountId cant not be null")
    @ApiModelProperty(name = "accountId", required = true)
    private Long accountId;

    @ApiModelProperty(name = "birthDay", required = false)
    private LocalDateTime birthDay;

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
