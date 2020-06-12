package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

/**
 * Created by fyh on 2020-6-12.
 */
@Data
public class DepartmentDomain {

    @ApiModelProperty(value = "组织机构id")
    @NotNull(message = "组织机构id不能为空")
    private Long organizationId;
    @ApiModelProperty(value = "父节点id")
    private Long parentId;
    @ApiModelProperty(value = "部门编号")
    private String departmentNumber;
    @ApiModelProperty(value = "部门名称")
    private String departmentName;
    @ApiModelProperty(value = "部门描述")
    private String description;
    @ApiModelProperty(value = "排序")
    private Integer sort;

}
