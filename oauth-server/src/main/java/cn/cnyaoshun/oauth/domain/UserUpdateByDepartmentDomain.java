package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * @ClassName UserDomain
 * @Description 用于用户调整部门
 * @Author fyh
 * Date 2020/6/1618:24
 */
@Data
public class UserUpdateByDepartmentDomain {

    @ApiModelProperty(value = "调整后的部门集合")
    private Set<Long> departmentIds;

    @ApiModelProperty(value = "需要调整的人集合")
    private Set<Long> userIds;

}
