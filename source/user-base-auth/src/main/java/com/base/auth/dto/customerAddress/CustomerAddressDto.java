package com.base.auth.dto.customerAddress;

import com.base.auth.dto.nation.NationDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class CustomerAddressDto {
    @ApiModelProperty(name = "id")
    private Long id;

    @ApiModelProperty(name = "username")
    private String username;

    @ApiModelProperty(name = "province")
    private NationDto province;

    @ApiModelProperty(name = "district")
    private NationDto district;

    @ApiModelProperty(name = "commune")
    private NationDto commune;

    @ApiModelProperty(name = "address")
    private String address;

    @ApiModelProperty(name = "type")
    private Integer type;

    @ApiModelProperty(name = "isDefault")
    private boolean defaultAddress;

}
