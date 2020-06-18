package cn.cnyaoshun.oauth.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @ClassName permissionDomain
 * @Description 修改权限信息
 * @Author fyh
 * Date 2020/6/179:57
 */
@Data
public class PermissionDomainV3 {

    @ApiModelProperty(name = "ID")
    private Long id;

    @ApiModelProperty(name = "权限名称非空")
    @NotNull(message = "权限名称不能为空")
    private String permissionName;

    @ApiModelProperty(name = "权限编码")
    private Integer permissionType;

    @ApiModelProperty(name = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "最后一次修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
