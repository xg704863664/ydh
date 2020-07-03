package cn.cnyaoshun.oauth.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 描述:修改用户信息
 * @author fyh
 * @date: 2020-6-4
 */
@Data
public class UserUpdateDomain {

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

    @ApiModelProperty(value = "部门ID")
    @NotNull(message = "部门ID不能为空")
    private Long departmentId;

    @ApiModelProperty(value = "最后一次修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
