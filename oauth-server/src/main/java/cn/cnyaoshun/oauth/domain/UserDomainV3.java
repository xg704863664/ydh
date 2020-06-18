package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName UserDomainV3
 * @Description 用于无条件获取所有用户名称
 * @Author fyh
 * Date 2020/6/1615:06
 */
@Data
public class UserDomainV3 {

    @ApiModelProperty(name = "用户名称")
    private Long id;

    @ApiModelProperty(name = "用户名称")
    private String userName;
}
