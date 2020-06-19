package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 描述账户信息,用于添加账户,新建账户得时候默认添加一个账户
 * @author yonghong.fan
 * @date 2020-6-9
 */
@Data
public class AccountDomain {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "账号")
    private String accountName;

    @ApiModelProperty(value = "密码")
    private  String password;

}
