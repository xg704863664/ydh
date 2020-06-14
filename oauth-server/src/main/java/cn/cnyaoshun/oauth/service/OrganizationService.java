package cn.cnyaoshun.oauth.service;

import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.domain.OrganizationDomain;
import cn.cnyaoshun.oauth.domain.OrganizationDomainV2;

/**
 * Created by fyh on 2020-6-11.
 */
public interface OrganizationService {

    Long add(OrganizationDomain organizationDomain);
    void delete(Long organizationId);
    Long update(OrganizationDomainV2 organizationDomainV2);
    PageDataDomain<OrganizationDomainV2> findAll();

}
