package cn.cnyaoshun.form.designer.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DesignerDomain {
    @ApiModelProperty(value = "id")
    @NotNull
    private Long id;
    @ApiModelProperty(value = "value")
    @NotBlank
    private String value;
}
