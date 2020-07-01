package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName permissionIDAndNameDomain
 * @Description DOTO
 * @Author fyh
 * Date 2020/7/117:21
 */
@Data
public class PermissionIDAndNameDomain {
    @ApiModelProperty(value = "权限ID")
    private Long id;

    @ApiModelProperty(value = "权限名称")
    private String permissionName;
}
