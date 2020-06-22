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
    Long countByUserId(Long departmentId);
    List<UserFindAllDomain> findAllUserName();
    //修改部门
    boolean reviseDepartment(UserUpdateByDepartmentDomain userUpdateByDepartmentDomain);

}
