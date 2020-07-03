package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 描述:新增用户信息
 * @author :fyh
 * @date: 2020-6-4
 */
@Data
public class UserAddDomain {

    @ApiModelProperty(value = "部门ID")
    private Long departmentId;

    @NotBlank(message = "姓名不能为空")
    @ApiModelProperty(value = "姓名不能为空")
    private String userName;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "电话;不能为空并且长度为11位")
    @NotBlank(message = "电话不能为空")
    @Length(min=11,max = 11)
    private String phone;

    @Email
    private String email;

    @NotBlank(message = "身份证号码不能为空")
    private String idNo;

}
