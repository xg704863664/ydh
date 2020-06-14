package cn.cnyaoshun.oauth.entity;

import cn.cnyaoshun.oauth.common.entity.AbstractEntity;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by fyh on 2020-6-4.
 * 用户信息表
 */
@Entity(name = "user")
@Data
public class User extends AbstractEntity{

    @Column(name = "user_no")
    private String userNo;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "sex")
    private String sex;
    @Column(name = "age")
    private Integer age;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;
    @Column(name = "id_type")
    private Integer idType;
    @Column(name = "id_no")
    private String idNo;
    @Column(name = "address")
    private String address;
    @Column(name = "state")
    private boolean state;

}
