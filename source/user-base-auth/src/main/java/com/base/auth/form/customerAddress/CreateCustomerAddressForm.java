package com.base.auth.form.customerAddress;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@ApiModel
public class CreateCustomerAddressForm {

    @NotNull(message = "provinceId cant not be null")
    @ApiModelProperty(name = "provinceId", required = true)
    private Long provinceId;

    @NotNull(message = "provinceId cant not be null")
    @ApiModelProperty(name = "districtId", required = true)
    private Long districtId;

    @NotNull(message = "provinceId cant not be null")
    @ApiModelProperty(name = "communeId", required = true)
    private Long communeId;

    @ApiModelProperty(name = "address", required = false)
    private String address;

    @NotNull(message = "Type cannot be null.")
    @Min(value = 1, message = "Type must be between 1 and 2.")
    @Max(value = 2, message = "Type must be between 1 and 2.")
    @ApiModelProperty(name = "type", required = true)
    private Integer type;

    @NotNull(message = "IsDefault cannot be null.")
    @ApiModelProperty(name = "defaultAddress", required = true)
    private Boolean defaultAddress;

}
