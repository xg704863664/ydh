package cn.cnyaoshun.oauth.service;

import cn.cnyaoshun.oauth.domain.DepartmentDomain;
import cn.cnyaoshun.oauth.domain.DepartmentDomainV2;
import cn.cnyaoshun.oauth.domain.DepartmentDomainV3;

import java.util.List;

/**
 * Created by fyh on 2020-6-12.
 */
public interface DepartmentService {

    Long add(DepartmentDomainV2 departmentDomainV2);
    void delete(Long departmentId);
    Long update(DepartmentDomainV3 departmentDomainV3);
    List<DepartmentDomain> findByOrganizationId(Long organizationId);

}
