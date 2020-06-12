package cn.cnyaoshun.oauth.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by fyh on 2020-6-4.
 */
@Data
public class UserDomainV2 {

    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "工号")
    private String userNumber;
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
    @ApiModelProperty(value = "证件类型;0:身份证;1:驾照;2:护照;3:港澳通行证")
    private Integer cardType;
    @ApiModelProperty(value = "证件编号")
    private String cardNo;
    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "状态 true：开启 false :关闭")
    private boolean state;
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date createTime;
    @ApiModelProperty(value = "最后一次修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
