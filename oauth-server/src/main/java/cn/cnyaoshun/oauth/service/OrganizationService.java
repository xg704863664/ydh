package cn.cnyaoshun.oauth.service;

import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.domain.OrganizationDomain;
import cn.cnyaoshun.oauth.domain.OrganizationDomainV2;
import cn.cnyaoshun.oauth.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Created by fyh on 2020-6-11.
 */
public interface OrganizationService {

    Long insertOrganization(OrganizationDomain organizationDomain);

    Long updateOrganization(OrganizationDomainV2 organizationDomainV2);
    PageDataDomain<OrganizationDomainV2> organizationList(Integer pageNumber, Integer pageSize);
    void deleteOrganization(Long organizationId);
}
