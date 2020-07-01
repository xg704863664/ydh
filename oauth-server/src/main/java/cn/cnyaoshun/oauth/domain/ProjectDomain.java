package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ProjectDomain
 * @Description 用于构建系统信息时使用
 * @Author fyh
 * Date 2020/6/3011:31
 */
@Data
public class ProjectDomain {

    @ApiModelProperty(value = "项目ID")
    private Long id;

    @ApiModelProperty(value = "项目名称")
    private String projectName;
}
