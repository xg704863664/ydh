package cn.cnyaoshun.form.designer.model;

import cn.cnyaoshun.form.common.entity.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity(name = "record")
public class Record extends AbstractEntity {

    /**
     * 填报数据的id
     */
    private String formId;

    /**
     * 填报人
     */
    private String filler;

    /**
     * 设计器id
     */
    private Long designerId;

}
