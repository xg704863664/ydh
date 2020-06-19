package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName OauthUserListDomain
 * @Description 根据项目ID,token 获取用户所有信息
 * @Author fyh
 * Date 2020/6/1910:15
 */
@Data
public class OauthUserListDomain {

    @ApiModelProperty(value = "账户ID")
    private Long accountId;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "角色ID的集合")
    private List<RoleDomainV3> roleList;

    @ApiModelProperty(value = "权限ID集合")
    private List<PermissionDomainOauthList> permissionList;
}
