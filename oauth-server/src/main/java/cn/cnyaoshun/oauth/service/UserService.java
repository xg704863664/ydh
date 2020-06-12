package cn.cnyaoshun.oauth.service;


import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.domain.AccountDomain;
import cn.cnyaoshun.oauth.domain.UserDomain;
import cn.cnyaoshun.oauth.domain.UserDomainV2;
import org.springframework.stereotype.Service;

/**
 * Created by fyh on 2020-6-4.
 * 用户表接口
 */
public interface UserService {
//    PageDataDomain<UserDomainV2> organizationList(Integer pageNumber, Integer pageSize, Long organizationId,String name);
    PageDataDomain<UserDomainV2> departmentList(Integer pageNumber, Integer pageSize, Long departmentId,String name);
    Long insertUser(UserDomain userDomain);
}
