package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName RoleDomain
 * @Description 根据项目ID,新增角色
 * @Author fyh
 * Date 2020-6-1511:47
 */
@Data
public class RoleAddDomain {

    @ApiModelProperty(value = "项目ID")
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    @ApiModelProperty(value = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    @ApiModelProperty(value = "权限ID")
    private List<Long> permissionIdList;

}
