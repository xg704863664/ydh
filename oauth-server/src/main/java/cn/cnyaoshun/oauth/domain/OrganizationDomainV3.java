package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *描述:无条件查询所有组织机构
 * @author fyh
 * @date: 202-6-11
 */
@Data
public class OrganizationDomainV3 {

    @ApiModelProperty(name = "ID")
    private  Long id;

    @ApiModelProperty(name = "公司名称")
    private String organizationName;

    @ApiModelProperty(name="公司地址")
    private String address;

    @ApiModelProperty(name = "电话")
    private String organizationPhone;

    @ApiModelProperty(name = "类型,用于前端,对后端无任何作用")
    private Integer type;

}
