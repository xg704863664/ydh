package cn.cnyaoshun.form.datasource.model;

import cn.cnyaoshun.form.common.entity.AbstractEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Data
@Entity(name = "data_source_config")
public class DataSourceConfig extends AbstractEntity {

    /**
     * 数据源名称
     */
    @ApiModelProperty(value = "数据源名称")
    private String name;

    /**
     * 编号
     */
    @ApiModelProperty(value = "数据源编号")
    private String code;

    /**
     * 类型
     * mysql/oracle
     */
    @ApiModelProperty(value = "数据源类型")
    @NotBlank(message = "数据库类型不能为空")
    private String type;

    /**
     * url
     */
    @ApiModelProperty(value = "数据源URL")
    @NotBlank(message = "URL不能为空")
    private String url;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "数据源用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(value = "数据源密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 创建人
     */
    private String createUserName;

    /**
     * 修改人
     */
    private String updateUserName;

}
