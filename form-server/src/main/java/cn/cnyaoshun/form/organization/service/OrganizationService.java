package cn.cnyaoshun.form.organization.service;

import cn.cnyaoshun.form.common.PageDataDomain;
import cn.cnyaoshun.form.organization.model.Organization;

import java.util.List;

/**
 * @Author:
 * @Date: 2020/7/8 17:46
 */

public interface OrganizationService {
    Organization save(Organization organization);
    List<Organization> getAll();
    void delete(Long id);
    PageDataDomain<Organization> findByPage(Integer pageNum, Integer pageSize,String name);

}
