package com.base.auth.form.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ProductCartForm {
    @NotNull(message = "id cant not be null")
    @ApiModelProperty(name = "id", required = true)
    Long productId;

    @NotNull(message = "quantity cant not be null")
    @Min(value = 1, message = "quantity value min 1")
    @ApiModelProperty(name = "quantity", required = true)
    Integer quantity;
}
