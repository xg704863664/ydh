package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fyh on 2020-6-12.
 * 用于新增部门
 */
@Data
public class DepartmentDomainV2 {

    @NotNull(message = "组织机构Id不能为空")
    @ApiModelProperty(value = "组织机构id")
    private Long organizationId;
    @ApiModelProperty(value = "父节点id")
    private Long parentId;
    @NotNull(message = "部门编号不能为空")
    @ApiModelProperty(value = "部门编号")
    private String departmentNumber;
    @NotNull(message = "部门名称不能为空")
    @ApiModelProperty(value = "部门名称")
    private String departmentName;
    @ApiModelProperty(value = "部门描述")
    private String description;

}
