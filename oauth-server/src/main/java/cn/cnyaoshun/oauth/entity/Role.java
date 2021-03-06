package cn.cnyaoshun.oauth.entity;

import cn.cnyaoshun.oauth.common.entity.AbstractEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by fyh on 2020-6-5.
 * 角色信息
 */
@Data
@Entity(name = "role")
public class Role extends AbstractEntity{

    @Column(name = "role_name")
    private String roleName;
    @Column(name = "project_id")
    private Long projectId;

}
