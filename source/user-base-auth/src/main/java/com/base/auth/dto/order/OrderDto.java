package com.base.auth.dto.order;

import com.base.auth.dto.customer.CustomerDto;
import com.base.auth.dto.orderItem.OrderItemDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    @ApiModelProperty(name = "orderId")
    private Long orderId;

    @ApiModelProperty(name = "code")
    private String code;

    @ApiModelProperty(name = "totalMoney")
    private Double totalMoney;

    @ApiModelProperty(name = "totalSellOff")
    private Double totalSellOff;

    @ApiModelProperty(name = "state")
    private Integer state;

    @ApiModelProperty(name = "customer")
    private CustomerDto customer;

    @ApiModelProperty(name = "orderItems")
    private List<OrderItemDto> orderItems;

}
