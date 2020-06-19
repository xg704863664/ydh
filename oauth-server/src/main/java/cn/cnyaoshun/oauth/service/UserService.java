package cn.cnyaoshun.oauth.service;


import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.domain.*;

import java.util.List;

/**
 * Created by fyh on 2020-6-4.
 * 用户表接口
 */
public interface UserService {

    Long add(UserDomain userDomain);
    Long delete(Long userId);
    Long update(UserDomainV2 userDomainV2);
    PageDataDomain<UserDomainV5> findAll(Long departmentId, String keyWord, Integer pageNumber, Integer pageSize);
    Long countByUserId(Long departmentId);
    List<UserDomainV3> findAllUserName();
    //修改部门
    boolean reviseDepartment(UserDomainV4 userDomainV4);

}
