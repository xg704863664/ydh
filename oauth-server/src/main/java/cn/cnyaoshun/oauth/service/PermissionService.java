package cn.cnyaoshun.oauth.service;

import cn.cnyaoshun.oauth.domain.PermissionDomain;
import cn.cnyaoshun.oauth.domain.PermissionDomainV2;
import cn.cnyaoshun.oauth.domain.PermissionDomainV3;

import java.util.List;

/**
 * Created by fyh on 2020/6/17.
 */
public interface PermissionService {

    Long add(PermissionDomain permissionDomain);
    Long delete(Long permissionId);
    List<PermissionDomainV2> findAllByProjectId(Long  projectId);
    Long update(PermissionDomainV3 permissionDomainV3);

}
