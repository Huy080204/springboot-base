package com.base.auth.form.nation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
public class CreateNationForm {
    @NotEmpty(message = "name cant not be null")
    @ApiModelProperty(name = "name", required = true)
    private String name;

    @NotEmpty(message = "description cant not be null")
    @ApiModelProperty(name = "description", required = true)
    private String description;

    @NotNull(message = "type cant not be null")
    @ApiModelProperty(name = "type", required = true)
    private Integer type;
}
