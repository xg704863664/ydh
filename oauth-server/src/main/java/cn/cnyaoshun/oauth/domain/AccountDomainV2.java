package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述账户信息
 * @author yonghong.fan
 * @date 2020-6-15
 */
@Data
public class AccountDomainV2 {

    @ApiModelProperty(name = "账户ID")
    private Long id;

    @ApiModelProperty(name = "账号")
    private String accountName;

    @ApiModelProperty(name = "用户名称")
    private String userName;

    @ApiModelProperty(name = "状态")
    private boolean state;

    @ApiModelProperty(name = "角色名称")
    private String RoleName;


}
