package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;

/**
 * 描述账户信息
 * @author yonghong.fan
 * @date 2020-6-9
 */
public class AccountDomain {

    @ApiModelProperty(name = "用户ID")
    private Long userId;

    @ApiModelProperty(name = "账号")
    private String accountName;

    @ApiModelProperty(name = "密码")
    private  String password;

    @ApiModelProperty(name = "头像")
    private String avatar;

}
