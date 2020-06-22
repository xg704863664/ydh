package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName RoleDomainV2
 * @Description 获取所有的角色列表,根据项目ID和TOKEN获取角色信息
 * @Author fyh
 * Date 2020-6-1515:02
 */
@Data
public class RoleFindAllByProjectIdAndAccountDomain {

    @ApiModelProperty(value = "角色ID")
    private Long id;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

}