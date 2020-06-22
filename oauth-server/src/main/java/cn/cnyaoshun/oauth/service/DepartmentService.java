package cn.cnyaoshun.oauth.service;

import cn.cnyaoshun.oauth.domain.DepartmentTreeDomain;
import cn.cnyaoshun.oauth.domain.DepartmentAddDomain;
import cn.cnyaoshun.oauth.domain.DepartmentUpdateDomain;

import java.util.List;

/**
 * Created by fyh on 2020-6-12.
 */
public interface DepartmentService {

    Long add(DepartmentAddDomain departmentAddDomain);
    void delete(Long departmentId);
    Long update(DepartmentUpdateDomain departmentUpdateDomain);
    List<DepartmentTreeDomain> findByOrganizationId(Long organizationId);

}
