package cn.cnyaoshun.form.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName RoleDomainV2
 * @Description 根据项目ID和TOKEN获取角色信息
 * @Author fyh
 * Date 2020-6-1515:02
 */
@Data
public class RoleFindAllByOauthDomain {

    @ApiModelProperty(value = "角色ID")
    private Long id;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

}
