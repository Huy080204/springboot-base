package com.base.auth.form.product;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class UpdateProductForm {

    @NotNull(message = "id cant not be null")
    @ApiModelProperty(name = "id", required = true)
    private Long id;

    @NotBlank(message = "name cant not be empty")
    @ApiModelProperty(name = "name", required = true)
    private String name;

    @ApiModelProperty(name = "description")
    private String description;

    @ApiModelProperty(name = "shortDescription")
    private String shortDescription;

    @ApiModelProperty(name = "price", required = false)
    private Double price;

    @ApiModelProperty(name = "sellOff", required = false)
    private Integer sellOff;

    @ApiModelProperty(name = "image", required = false)
    private String image;

}
