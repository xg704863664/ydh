package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;


/**
 * 描述部门对象,用于新增部门
 * @author yonghong.fan
 * @date 2020-6-9
 */

@Data
public class DepartmentAddDomain {

    @NotNull(message = "组织机构ID不能为空")
    @ApiModelProperty(value = "组织机构ID")
    private Long organizationId;

    @ApiModelProperty(value = "父ID")
    private Long parentId;

    @NotNull(message = "部门编号不能为空")
    @ApiModelProperty(value = "部门编号")
    private String departmentNo;

    @NotNull(message = "部门名称不能为空")
    @ApiModelProperty(value = "部门名称")
    private String departmentName;
}
