package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName permissionDomain
 * @Description 根据项目ID,查询权限信息
 * @Author fyh
 * Date 2020/6/179:57
 */
@Data
public class PermissionDomainV2 {

    @ApiModelProperty(name = "项目ID")
    private Long projectId;

    @ApiModelProperty(name = "权限名称非空")
    @NotNull(message = "权限名称不能为空")
    private String permissionName;

    @ApiModelProperty(name = "权限编码")
    private Integer permissionType;

}
