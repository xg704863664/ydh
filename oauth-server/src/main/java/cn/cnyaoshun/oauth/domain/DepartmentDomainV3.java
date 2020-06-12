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
    @ApiModelProperty(value = "id")
    private  Long id;
    @ApiModelProperty(value = "组织机构id")
    private Long organizationId;
    @ApiModelProperty(value = "父节点id")
    private Long parentId;
    @ApiModelProperty(value = "部门编号")
    private String departmentNumber;
    @ApiModelProperty(value = "部门名称")
    private String departmentName;
    @ApiModelProperty(value = "部门描述")
    private String description;
    @ApiModelProperty(value = "子部门")
    List<DepartmentDomain> children = new ArrayList<>();
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date createTime;
    @ApiModelProperty(value = "最后一次修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
