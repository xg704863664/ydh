package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName RoleDomainV2
 * @Description 获取所有的角色列表
 * @Author fyh
 * Date 2020-6-1515:02
 */
@Data
public class RoleFindAllByProjectIdAndAccountDomain {

    @ApiModelProperty(value = "角色ID")
    private Long id;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "所属系统ID")
    private Long projectId;

    @ApiModelProperty(value = "所属系统")
    private String projectName;

    @ApiModelProperty(value = "权限列表")
    private List<PermissionIDAndNameDomain> permissionList;

}
