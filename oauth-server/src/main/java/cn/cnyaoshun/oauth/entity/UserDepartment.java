package cn.cnyaoshun.oauth.entity;

import cn.cnyaoshun.oauth.common.entity.AbstractOnlyIdEntity;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity(name = "user_department")
public class UserDepartment extends AbstractOnlyIdEntity {

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "organization_id")
    private Long organizationId;
}
