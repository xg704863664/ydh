package cn.cnyaoshun.oauth.service;


import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.domain.UserDomainV3;
import cn.cnyaoshun.oauth.domain.UserDomain;
import cn.cnyaoshun.oauth.domain.UserDomainV2;
import cn.cnyaoshun.oauth.domain.UserDomainV4;

import java.util.List;

/**
 * Created by fyh on 2020-6-4.
 * 用户表接口
 */
public interface UserService {

    Long add(UserDomain userDomain);
    Long delete(Long userId);
    Long update(UserDomainV2 userDomainV2);
    PageDataDomain<UserDomainV2> findAll(Long departmentId, String keyWord, Integer pageNumber, Integer pageSize);
    Long countByUserId(Long departmentId);
    List<UserDomainV3> findAllUserName();
    //修改部门
    boolean reviseDepartment(UserDomainV4 userDomainV4);

}
