package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 根据角色和用户添加账户
 * @author yonghong.fan
 * @date 2020-6-9
 */
@Data
public class AccountDomainV3 {

    @ApiModelProperty(name = "用户ID")
    private Long userId;

    @ApiModelProperty(name = "账号,不能为空")
    @NotNull(message = "账号不能为空")
    private String accountName;

    @ApiModelProperty(name = "密码,不能为空")
    @NotNull(message = "密码不能为空")
    private  String password;

    @ApiModelProperty(name = "分配角色列表")
    private List<Long> roleIdList;



}
