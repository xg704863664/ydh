package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName RoleDomain
 * @Description I根据项目ID,新增角色
 * @Author fyh
 * Date 2020-6-1511:47
 */
@Data
public class RoleDomain {

    @ApiModelProperty(value = "项目")
    private Long projectId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "角色类型;0:超级用户;1:管理员;2:项目负责人;3:一般用户;4:其他")
    private Integer roleType;

    @ApiModelProperty(value = "状态;0:冻结;1:开启")
    private Integer state;

}
