package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by fyh on 2020-6-12.
 * 修改使用
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
