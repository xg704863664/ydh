package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Created by fyh on 2020-6-4.
 */
@Data
public class UserDomain {

    @NotBlank(message = "工号不能为空")
    @ApiModelProperty(value = "工号")
    private String userNumber;
    @NotBlank(message = "姓名不能为空")
    @ApiModelProperty(value = "姓名")
    private String userName;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "年龄")
    private Integer age;
    @ApiModelProperty(value = "电话")
    @NotBlank(message = "电话不能为空")
    @Length(min=11,max = 11)
    private String phone;
    @Email
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "证件类型;0:身份证;1:驾照;2:护照;3:港澳通行证")
    private Integer cardType;
    @ApiModelProperty(value = "证件编号")
    private String cardNo;
    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "部门id")
    private Long departmentId;
    @ApiModelProperty(value = "机构id")
    private Long organizationId;
}
