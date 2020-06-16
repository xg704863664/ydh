package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 根据角色和用户添加账户
 * @author yonghong.fan
 * @date 2020-6-9
 */
@Data
public class AccountDomainV3 {

    @ApiModelProperty(name = "角色ID")
    private Long roleId;

    @ApiModelProperty(name = "用户名称")
    private String userName;

    @ApiModelProperty(name = "账号")
    private String accountName;

    @ApiModelProperty(name = "密码")
    private  String password;

    @ApiModelProperty(name = "状态")
    private boolean state;

}
