package cn.cnyaoshun.oauth.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName RoleDomainV2
 * @Description 获取所有的角色列表
 * @Author fyh
 * Date 2020-6-1515:02
 */
@Data
public class RoleDomainV3 {

    @ApiModelProperty(value = "角色ID")
    private Long id;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

}
