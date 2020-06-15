package cn.cnyaoshun.oauth.service;

import cn.cnyaoshun.oauth.domain.RoleDomain;
import cn.cnyaoshun.oauth.domain.RoleDomainV2;

/**
 * Created by fyh on 2020-6-15.
 */
public interface RoleService {

    Long add(RoleDomain roleDomain);
    Long delete(Long roleId);
    Long update(RoleDomainV2 roleDomainV2);
}
