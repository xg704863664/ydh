package cn.cnyaoshun.oauth.service;


import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.domain.UserDomain;
import cn.cnyaoshun.oauth.domain.UserDomainV2;

/**
 * Created by fyh on 2020-6-4.
 * 用户表接口
 */
public interface UserService {
    PageDataDomain<UserDomainV2> departmentList(Integer pageNumber, Integer pageSize, Long departmentId,String name);
    Long insertUser(UserDomain userDomain);
    Long updateUser(UserDomainV2 userDomainV2);
    void deleteUser(Long userId);
}
