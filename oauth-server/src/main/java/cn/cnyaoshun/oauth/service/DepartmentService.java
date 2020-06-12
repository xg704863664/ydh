package cn.cnyaoshun.oauth.service;

import cn.cnyaoshun.oauth.domain.DepartmentDomain;

import java.util.List;
import java.util.Map;

/**
 * Created by fyh on 2020-6-12.
 */
public interface DepartmentService {
    Map<Long ,List<DepartmentDomain>> findByOrganizationId(Long organizationId);
}
