package cn.cnyaoshun.form.common.domain;

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

    @ApiModelProperty(value = "角色集合")
    private List<RoleFindAllByOauthDomain> roleList;

    @ApiModelProperty(value = "权限集合")
    private List<PermissionOauthUserListDomain> permissionList;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "组织机构id")
    private Long orgId;
}
