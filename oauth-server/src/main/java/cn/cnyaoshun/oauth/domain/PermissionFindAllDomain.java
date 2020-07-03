package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName PermissionFindAllDomain
 * @Description 获取所有系统的权限
 * @Author fyh
 * Date 2020/6/2915:31
 */
@Data
public class PermissionFindAllDomain {

    @ApiModelProperty(value = "权限名称非空")
    @NotBlank(message = "权限名称不能为空")
    private String permissionName;

    @ApiModelProperty(value = "权限编码")
    private String permissionType;

    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "权限ID")
    private Long id;

}
