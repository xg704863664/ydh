package cn.cnyaoshun.oauth.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * Created by fyh on 2020-6-11.
 */

@Data
public class OrganizationDomain {

    @ApiModelProperty(name = "公司名称")
    @NotBlank(message = "公司名称不能为空")
    private String organizationName;

    @ApiModelProperty(name="公司地址")
    private String address;

    @ApiModelProperty(name = "公司描述")
    private String description;

}
