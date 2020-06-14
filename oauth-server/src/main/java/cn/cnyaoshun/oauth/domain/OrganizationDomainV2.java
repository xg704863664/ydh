package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *描述:修改组织机构
 * @author fyh
 * @date: 202-6-11
 */
@Data
public class OrganizationDomainV2 {

    @ApiModelProperty(name = "ID")
    private  Long id;

    @ApiModelProperty(name = "公司名称")
    private String organizationName;

    @ApiModelProperty(name="公司地址")
    private String address;

    @ApiModelProperty(name = "公司描述")
    private String description;

    @ApiModelProperty(name = "状态")
    private boolean state;

}
