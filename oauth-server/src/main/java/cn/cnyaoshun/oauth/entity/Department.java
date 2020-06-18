package cn.cnyaoshun.oauth.entity;


import cn.cnyaoshun.oauth.common.entity.AbstractEntity;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by fyh on 2020-6-5.
 * 部门信息
 */
@Data
@Entity(name = "department")
public class Department extends AbstractEntity{

    @Column(name = "organization_id")
    private Long organizationId;
    @Column(name = "parent_id")
    private Long parentId;
    @Column(name = "department_no")
    private String departmentNo;
    @Column(name = "department_name")
    private String departmentName;;

}
