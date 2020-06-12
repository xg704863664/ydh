package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.dao.OrganizationRepository;
import cn.cnyaoshun.oauth.domain.OrganizationDomain;
import cn.cnyaoshun.oauth.domain.OrganizationDomainV2;
import cn.cnyaoshun.oauth.entity.Organization;
import cn.cnyaoshun.oauth.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

/**
 * Created by fyh on 2020-6-11.
 */
@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService{

    public final OrganizationRepository organizationRepository;

    /**
     * 新增
     * @param organizationDomain
     * @return
     */
    @Override
    @Transactional
    public Long insertOrganization(OrganizationDomain organizationDomain){
        Organization organization = new Organization();
        BeanUtils.copyProperties(organizationDomain, organization);
        organization.setState(true);
        Organization SaveOrganization = organizationRepository.save(organization);
        return SaveOrganization.getId();
    }

    /**
     * 修改
     * @param organizationDomainV2
     * @return
     */
    @Override
    @Transactional
    public Long updateOrganization(OrganizationDomainV2 organizationDomainV2){
        Optional<Organization> organizationOptional = organizationRepository.findById(organizationDomainV2.getId());
        //满足条件时(organizationOptional为true时)进入下面条件
        organizationOptional.ifPresent(organization -> {
            BeanUtils.copyProperties(organizationDomainV2,organization);
            organization.setId(organization.getId());
            organization.setUpdateTime(new Date());
            organizationRepository.save(organization);
        });
        return organizationDomainV2.getId();
    }

    /**
     * 查询
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public PageDataDomain<OrganizationDomainV2> organizationList(Integer pageNumber, Integer pageSize){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        Pageable page = PageRequest.of(pageNumber,pageSize,sort);
        Page<Organization> organizationPage = organizationRepository.findAll(page);
        PageDataDomain<OrganizationDomainV2> pageDataDomain = new PageDataDomain();
        //当前页
        pageDataDomain.setCurrent(pageNumber);
        //总页数
        pageDataDomain.setPages(pageSize);
        //总条数
        pageDataDomain.setTotal(organizationPage.getTotalElements());
        organizationPage.getContent().forEach(organization -> {
            OrganizationDomainV2 organizationDomainV2 =  new OrganizationDomainV2();
            BeanUtils.copyProperties(organization,organizationDomainV2);
            pageDataDomain.getRecords().add(organizationDomainV2);
        });
        return pageDataDomain;
    }

    @Override
    @Transactional
    public void deleteOrganization(Long organizationId){
        organizationRepository.deleteById(organizationId);
    }
}
