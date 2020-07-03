package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 *描述:修改组织机构
 * @author fyh
 * @date: 202-6-11
 */
@Data
public class OrganizationUpdateDomain {

    @ApiModelProperty(value = "ID")
    private  Long id;

    @NotBlank(message = "公司名称不能为空")
    @ApiModelProperty(value = "公司名称")
    private String organizationName;

    @ApiModelProperty(value="公司地址")
    private String address;

    @ApiModelProperty(value = "电话")
    private String organizationPhone;

}
