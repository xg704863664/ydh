package cn.cnyaoshun.oauth.service;

import cn.cnyaoshun.oauth.domain.RoleDomain;
import cn.cnyaoshun.oauth.domain.RoleDomainV2;
import cn.cnyaoshun.oauth.domain.RoleDomainV3;

import java.util.List;

/**
 * Created by fyh on 2020-6-15.
 */
public interface RoleService {

    Long add(RoleDomain roleDomain);
    Long delete(Long roleId);
    List<RoleDomainV2> findAllByProjectId(Long projectId);
    Long update(RoleDomainV2 roleDomainV2);
    List<RoleDomainV3> findAll();

}
