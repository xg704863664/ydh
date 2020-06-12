package cn.cnyaoshun.oauth.entity;

import cn.cnyaoshun.oauth.common.entity.AbstractOnlyIdEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity(name = "role_permission")
public class RolePermission extends AbstractOnlyIdEntity {

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "permission_id")
    private Long permissiond;
}
