package com.base.auth.form.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class UpdateOrderForm {
    @NotNull(message = "Order id cant not be null")
    @ApiModelProperty(name = "orderId", required = true)
    Long orderId;

    @NotNull(message = "Order state cant not be null")
    @Min(value = 1, message = "Order state must be at least 1")
    @Max(value = 5, message = "Order state must be at most 5")
    @ApiModelProperty(name = "state", required = true)
    Integer state;
}
