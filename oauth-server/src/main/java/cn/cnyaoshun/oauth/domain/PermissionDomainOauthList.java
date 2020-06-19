package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName permissionDomain
 * @Description 根据项目ID和TOKEN获取权限信息
 * @Author fyh
 * Date 2020/6/179:57
 */
@Data
public class PermissionDomainOauthList {

    @ApiModelProperty(value = "权限ID")
    private Long id;

    @ApiModelProperty(value = "权限名称非空")
    private String permissionName;

    @ApiModelProperty(value = "权限编码")
    private Integer permissionType;

}
