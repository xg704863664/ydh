package cn.cnyaoshun.oauth.service;

import cn.cnyaoshun.oauth.domain.OrganizationAddDomain;
import cn.cnyaoshun.oauth.domain.OrganizationUpdateDomain;
import cn.cnyaoshun.oauth.domain.OrganizationFindAllDomain;

import java.util.List;

/**
 * Created by fyh on 2020-6-11.
 */
public interface OrganizationService {

    Long add(OrganizationAddDomain organizationAddDomain);
    void delete(Long organizationId);
    Long update(OrganizationUpdateDomain organizationUpdateDomain);
    List<OrganizationFindAllDomain> findAll();

}
