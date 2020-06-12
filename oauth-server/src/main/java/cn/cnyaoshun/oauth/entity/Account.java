package cn.cnyaoshun.oauth.entity;

import cn.cnyaoshun.oauth.common.entity.AbstractEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by fyh on 2020-6-5.
 * 账户信息表
 */
@Data
@Entity(name = "account")
public class Account extends AbstractEntity {

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "account_name")
    private String accountName;
    @Column(name = "password")
    private  String password;
    @Column(name = "avatar")
    private String avatar;
    @Column(name = "state")
    private boolean state;

}
