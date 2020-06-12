package cn.cnyaoshun.oauth.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fyh on 2020-6-12.
 * 修改使用
 */
@Data
public class DepartmentDomainV3 {

    @ApiModelProperty(value = "部门id")
    private Long departmentId;
    @ApiModelProperty(value = "部门名称")
    private String departmentName;
    @ApiModelProperty(value = "父节点id")
    private Long parentId;

}
