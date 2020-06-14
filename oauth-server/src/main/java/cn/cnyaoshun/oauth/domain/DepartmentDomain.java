package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述部门对象,用于前端进行树形结构的展示
 * @author yonghong.fan
 * @date 2020-6-9
 */

@Data
public class DepartmentDomain {
    @ApiModelProperty(value = "ID")
    private  Long id;

    @ApiModelProperty(value = "组织机构ID")
    private Long organizationId;

    @ApiModelProperty(value = "父ID")
    private Long parentId;

    @ApiModelProperty(value = "部门编号")
    private String departmentNo;

    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    @ApiModelProperty(value = "部门描述")
    private String description;

    @ApiModelProperty(value = "子部门")
    List<DepartmentDomain> children = new ArrayList<>();
}
