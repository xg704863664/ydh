package cn.cnyaoshun.file.entity;

import cn.cnyaoshun.file.common.entity.AbstractOnlyIdEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Data
@Entity(name = "file_record")
public class FileRecord extends AbstractOnlyIdEntity {

    /**
     * 文件名称
     */
    @Column(name = "name")
    private String name;
    /**
     * 文件存储路径
     */
    @Column(name = "path")
    private String path;
    /**
     * 文件类型
     */
    @Column(name = "file_type")
    private String fileType;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime = new Date();
}
