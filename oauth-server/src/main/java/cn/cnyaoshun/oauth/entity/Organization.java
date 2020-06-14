package cn.cnyaoshun.oauth.entity;

import cn.cnyaoshun.oauth.common.entity.AbstractEntity;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by fyh on 2020-6-5.
 * 组织机构
 */
@Data
@Entity(name = "organization")
public class Organization extends AbstractEntity{

    @Column(name = "organization_name")
    private String organizationName;
    @Column(name = "address")
    private String address;
    @Column(name = "description")
    private String description;
    @Column(name = "state")
    private boolean state;

}
