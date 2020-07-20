package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName UserFindAllByOrgDomain
 * @Description DOTO
 * @Author fyh
 * Date 2020/7/2013:38
 */
@Data
public class UserFindAllByOrgDomain {

    @ApiModelProperty(value = "部门名称")
    private String departmentName;

    @NotBlank(message = "ID不能为空")
    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "证件编号")
    private String idNo;

}
