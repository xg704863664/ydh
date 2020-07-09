package cn.cnyaoshun.form.designer.model;

import cn.cnyaoshun.form.common.entity.AbstractEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    private String value;

    /**
     * 状态位
     */
    @ApiModelProperty(value = "status")
    private boolean status;

    /**
     * 创建人姓名
     */
    @ApiModelProperty(value = "createUserName")
    private String createUserName;

    /**
     * 修改人姓名
     */
    @ApiModelProperty(value = "updateUserName")
    private String updateUserName;

    /**
     * 树id
     */
    @ApiModelProperty(value = "orgId")
    private Long orgId;

    /**
     * 数据源id
     */
    @ApiModelProperty(value = "dataSourceId")
    private Long dataSourceId;

    /**
     * 表名
     */
    @ApiModelProperty(value = "tableName")
    private String tableName;

}
