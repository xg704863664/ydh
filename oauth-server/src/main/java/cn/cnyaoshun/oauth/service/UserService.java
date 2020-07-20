package cn.cnyaoshun.oauth.service;


import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.domain.*;

import java.util.List;

/**
 * Created by fyh on 2020-6-4.
 * 用户表接口
 */
public interface UserService {

    Long add(UserAddDomain userAddDomain);
    Long delete(Long userId);
    Long update(UserUpdateDomain userUpdateDomain);
    PageDataDomain<UserFindAllByDepartmentIdDomain> findAll(Long departmentId, String keyWord, Integer pageNumber, Integer pageSize);
    List<UserFindAllDomain> findAllUserName();
    boolean reviseDepartment(UserUpdateByDepartmentDomain userUpdateByDepartmentDomain);
    PageDataDomain<UserFindAllByOrgDomain> findAllUserByOrg(Long organizationId, String keyWord, Integer pageNumber, Integer pageSize);
}
