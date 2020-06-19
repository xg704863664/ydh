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

    @ApiModelProperty(value = "ID")
    private  Long id;

    @ApiModelProperty(value = "公司名称")
    private String organizationName;

    @ApiModelProperty(value="公司地址")
    private String address;

    @ApiModelProperty(value = "电话")
    private String organizationPhone;

    @ApiModelProperty(value = "类型,用于前端,对后端无任何作用")
    private Integer type;

}
