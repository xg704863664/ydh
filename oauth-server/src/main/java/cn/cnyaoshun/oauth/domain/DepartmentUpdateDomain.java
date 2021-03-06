package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 描述: 修改部门名称
 * @author fyh
 * @date: 2020-6-12
 */
@Data
public class DepartmentUpdateDomain {

    @ApiModelProperty(value = "部门ID")
    @NotNull(message = "部门ID不能为空")
    private Long departmentId;

    @NotBlank(message = "部门名称不能为空")
    @ApiModelProperty(value = "部门名称")
    private String departmentName;

}
