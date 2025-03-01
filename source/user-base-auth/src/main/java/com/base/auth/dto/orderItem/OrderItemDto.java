package com.base.auth.dto.orderItem;

import com.base.auth.dto.customer.CustomerDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderItemDto {
    @ApiModelProperty(name = "orderItemId")
    private Long orderItemId;

    @ApiModelProperty(name = "productName")
    private String productName;

    @ApiModelProperty(name = "quantity")
    private Integer quantity;

    @ApiModelProperty(name = "singlePrice")
    private Double singlePrice;

    @ApiModelProperty(name = "sellOff")
    private Double sellOff;


}
