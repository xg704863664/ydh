package cn.cnyaoshun.oauth.service;


import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.domain.UserDomain;
import cn.cnyaoshun.oauth.domain.UserDomainV2;

/**
 * Created by fyh on 2020-6-4.
 * 用户表接口
 */
public interface UserService {

    Long add(UserDomain userDomain);
    void delete(Long userId);
    Long update(UserDomainV2 userDomainV2);
    PageDataDomain<UserDomainV2> findAll(Long departmentId, String name, Integer pageNumber, Integer pageSize);

}
