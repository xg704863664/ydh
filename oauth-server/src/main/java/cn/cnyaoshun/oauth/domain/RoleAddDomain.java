package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName RoleDomain
 * @Description I根据项目ID,新增角色
 * @Author fyh
 * Date 2020-6-1511:47
 */
@Data
public class RoleAddDomain {

    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "权限ID")
    private List<Long> permissionIdList;

}
