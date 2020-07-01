package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName AccountFindAllDomain
 * @Description 获取所有账户信息
 * @Author fyh
 * Date 2020/6/3017:42
 */
@Data
public class AccountFindAllDomain {

    @ApiModelProperty(value = "账户ID")
    private Long id;

    @ApiModelProperty(value = "账号")
    private String accountName;

    @ApiModelProperty(value = "角色ID")
    private Long userId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "状态")
    private boolean state;

    @ApiModelProperty(value = "账号对应角色信息")
    private List<RoleDomain> roleList;

    @ApiModelProperty(value = "角色名称")
    private List<String> RoleName;

}
