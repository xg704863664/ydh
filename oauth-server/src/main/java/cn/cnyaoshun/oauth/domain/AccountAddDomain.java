package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 添加账户并分配角色
 * @author yonghong.fan
 * @date 2020-6-9
 */
@Data
public class AccountAddDomain {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "账号,不能为空")
    @NotNull(message = "账号不能为空")
    private String accountName;

    @ApiModelProperty(value = "密码,不能为空")
    @NotNull(message = "密码不能为空")
    private  String password;

    @ApiModelProperty(value = "分配角色列表")
    private List<Long> roleIdList;

}
