package cn.cnyaoshun.form.designer.model;

import cn.cnyaoshun.form.common.entity.AbstractEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity(name = "designer")
public class Designer extends AbstractEntity {

    /**
     * 表单名称
     */
    @ApiModelProperty(value = "name")
    private String name;

    /**
     * 表单编号
     */
    @ApiModelProperty(value = "code")
    private String code;

    /**
     * 表单json字符串
     */
    @ApiModelProperty(value = "value")
    @Column(columnDefinition = "varchar(4000)")
    private String value;

    /**
     * 树id
     */
    @ApiModelProperty(value = "orgId")
    private String orgId;

    /**
     * 数据源id
     */
    @ApiModelProperty(value = "dataSourceId")
    private String dataSourceId;

    /**
     * 表名
     */
    @ApiModelProperty(value = "tableName")
    private String tableName;

}
