package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ProjectRoleTreeDomain
 * @Description DOTO
 * @Author fyh
 * Date 2020/6/3018:57
 */
@Data
public class ProjectRoleTreeDomain {

    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "项目关联角色子树")
    private List<RoleDomain> children = new ArrayList<>();
}
