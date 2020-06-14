package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述: 修改部门名称
 * @author fyh
 * @date: 2020-6-12
 */
@Data
public class DepartmentDomainV3 {

    @ApiModelProperty(value = "部门ID")
    private Long departmentId;

    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    @ApiModelProperty(value = "父ID")
    private Long parentId;

}
