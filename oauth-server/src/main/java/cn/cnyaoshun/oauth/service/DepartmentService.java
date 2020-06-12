package cn.cnyaoshun.oauth.service;

import cn.cnyaoshun.oauth.domain.DepartmentDomain;
import cn.cnyaoshun.oauth.domain.DepartmentDomainV2;
import cn.cnyaoshun.oauth.domain.DepartmentDomainV3;

import java.util.List;

/**
 * Created by fyh on 2020-6-12.
 */
public interface DepartmentService {
    List<DepartmentDomain> findByOrganizationId(Long organizationId);
    Long insertDepartment(DepartmentDomainV2 departmentDomainV2);
    void deleteDepartment(Long departmentId);
    Long updateDepartment(DepartmentDomainV3 departmentDomainV3);
}
