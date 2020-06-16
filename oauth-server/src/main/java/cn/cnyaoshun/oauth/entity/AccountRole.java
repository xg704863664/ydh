package cn.cnyaoshun.oauth.entity;

import cn.cnyaoshun.oauth.common.entity.AbstractOnlyIdEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @ClassName AccountRole
 * @Description 账户角色关联信息
 * @Author fyh
 * Date 2020-6-1516:40
 */
@Data
@Entity(name = "account_role")
public class AccountRole extends AbstractOnlyIdEntity {

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "role_id")
    private Long roleId;
}
