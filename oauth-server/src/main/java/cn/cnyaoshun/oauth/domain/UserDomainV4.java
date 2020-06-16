package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @ClassName UserDomainV4
 * @Description 用于用户调整部门
 * @Author fyh
 * Date 2020/6/1618:24
 */
@Data
public class UserDomainV4 {

    @ApiModelProperty(value = "调整后的部门集合")
    Set<Long> departmentIds;

    @ApiModelProperty(value = "需要调整的人集合")
    Set<Long> userIds;
}
