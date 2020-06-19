package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * @ClassName AccountDomainV5
 * @Description 根据角色ID,分配账户
 * @Author fyh
 * Date 2020/6/1619:42
 */
@Data
public class AccountDomainV5 {

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "账户名称")
    private Set<String> accountName;
}
