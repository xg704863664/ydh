package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName RoleDomain
 * @Description DOTO
 * @Author fyh
 * Date 2020/6/3018:20
 */
@Data
public class RoleDomain {

    @ApiModelProperty(value = "角色ID")
    private Long id;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "项目ID")
    private Long projectId;

}
