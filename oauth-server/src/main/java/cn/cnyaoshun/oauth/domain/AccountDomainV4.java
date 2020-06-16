package cn.cnyaoshun.oauth.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName AccountDomainV4
 * @Description DOTO
 * @Author fyh
 * Date 2020/6/1616:00
 */
@Data
public class AccountDomainV4 {

    @ApiModelProperty(name = "账号ID")
    private Long id;

    @ApiModelProperty(name = "角色ID")
    private Long roleId;

    @ApiModelProperty(name = "用户名称")
    private String userName;

    @ApiModelProperty(name = "账号")
    private String accountName;

    @ApiModelProperty(name = "密码")
    private  String password;

    @ApiModelProperty(name = "状态")
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
