package cn.cnyaoshun.oauth.entity;

import cn.cnyaoshun.oauth.common.entity.AbstractEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by fyh on 2020-6-4.
 * 用户信息表
 */
@Entity(name = "crm_user")
@Data
public class User extends AbstractEntity{

    @Column(name = "user_number")
    private String userNumber;
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
    @Column(name = "card_type")
    private Integer cardType;
    @Column(name = "card_no")
    private String cardNo;
    @Column(name = "address")
    private String address;
    @Column(name = "state")
    private boolean state;

}
