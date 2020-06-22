package cn.cnyaoshun.oauth.service;

import cn.cnyaoshun.oauth.domain.PermissionAddDomain;
import cn.cnyaoshun.oauth.domain.PermissionFindAllByProjectIdDomain;
import cn.cnyaoshun.oauth.domain.PermissionUpdateDomain;

import java.util.List;

/**
 * Created by fyh on 2020/6/17.
 */
public interface PermissionService {

    Long add(PermissionAddDomain permissionAddDomain);
    Long delete(Long permissionId);
    List<PermissionFindAllByProjectIdDomain> findAllByProjectId(Long  projectId);
    Long update(PermissionUpdateDomain permissionUpdateDomain);

}
