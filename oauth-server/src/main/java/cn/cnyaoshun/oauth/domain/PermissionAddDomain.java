package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @ClassName permissionDomain
 * @Description 根据项目ID,新增权限
 * @Author fyh
 * Date 2020/6/179:57
 */
@Data
public class PermissionAddDomain {

    @ApiModelProperty(value = "权限名称非空")
    @NotBlank(message = "权限名称不能为空")
    private String permissionName;

    @NotBlank(message = "权限编码不能为空")
    @ApiModelProperty(value = "权限编码")
    private String permissionType;

    @NotNull(message = "项目ID不能为空")
    @ApiModelProperty(value = "项目ID")
    private Long projectId;
}
