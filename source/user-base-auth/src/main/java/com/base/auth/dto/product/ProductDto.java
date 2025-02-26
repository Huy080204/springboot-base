package com.base.auth.dto.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProductDto {
    @ApiModelProperty(name = "id")
    private Long productId;

    @ApiModelProperty(name = "name")
    private String name;

    @ApiModelProperty(name = "description")
    private String description;

    @ApiModelProperty(name = "shortDescription")
    private String shortDescription;

    @ApiModelProperty(name = "price")
    private Double price;

    @ApiModelProperty(name = "sellOff")
    private Integer sellOff;

    @ApiModelProperty(name = "image")
    private String image;
}
