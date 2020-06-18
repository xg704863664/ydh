package cn.cnyaoshun.oauth.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @ClassName AccountDomainV4
 * @Description 修改账户信息
 * @Author fyh
 * Date 2020/6/1616:00
 */
@Data
public class AccountDomainV4 {

    @ApiModelProperty(name = "账号ID")
    private Long id;

    @ApiModelProperty(name = "用户名称")
    private String userName;

    @ApiModelProperty(name = "账号,不能为空")
    @NotNull(message = "账号不能为空")
    private String accountName;

    @ApiModelProperty(name = "密码")
    @NotNull(message = "密码不能为空")
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

    @ApiModelProperty(value = "角色ID集合")
    private List<Long> roleIdList;
}
