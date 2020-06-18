package cn.cnyaoshun.oauth.entity;

import cn.cnyaoshun.oauth.common.entity.AbstractEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by fyh on 2020-6-5.
 */
@Data
@Entity(name = "permission")
public class Permission extends AbstractEntity{

    @Column(name = "permission_name")
    private String permissionName;
    @Column(name = "description")
    private String description;
    @Column(name = "permission_type")
    private Integer permissionType;
    @Column(name = "state")
    private boolean state;
    @Column(name = "project_id")
    private Long projectId;

}
