package cn.cnyaoshun.form.organization.model;

import cn.cnyaoshun.form.common.entity.AbstractEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

/**
 * @Author:
 * @Date: 2020/7/8 11:03
 */
@Data
@Entity(name = "organization")
public class Organization extends AbstractEntity {

    @ApiModelProperty("表单组织目录名")
    @NotBlank
    private String name;

    @ApiModelProperty("描述")
    @NotBlank
    private String description;

}
