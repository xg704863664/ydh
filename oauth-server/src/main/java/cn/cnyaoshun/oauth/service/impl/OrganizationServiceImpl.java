package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.dao.DepartmentRepository;
import cn.cnyaoshun.oauth.dao.OrganizationRepository;
import cn.cnyaoshun.oauth.dao.UserDepartmentRepository;
import cn.cnyaoshun.oauth.dao.UserRepository;
import cn.cnyaoshun.oauth.domain.OrganizationDomain;
import cn.cnyaoshun.oauth.domain.OrganizationDomainV2;
import cn.cnyaoshun.oauth.entity.Organization;
import cn.cnyaoshun.oauth.entity.UserDepartment;
import cn.cnyaoshun.oauth.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.springframework.aop.framework.AopContext.currentProxy;

/**
 * Created by fyh on 2020-6-11.
 */
@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService{

    private final OrganizationRepository organizationRepository;

    private final DepartmentRepository departmentRepository;

    private final UserDepartmentRepository userDepartmentRepository;

    private final UserRepository userRepository;
    /**
     * 新增
     * @param organizationDomain
     * @return
     */
    @Override
    @Transactional
    public Long add(OrganizationDomain organizationDomain){
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
    public Long update(OrganizationDomainV2 organizationDomainV2){
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
     * @return
     */
    @Override
    public PageDataDomain<OrganizationDomainV2> findAll(){
        PageDataDomain<OrganizationDomainV2> pageDataDomain = new PageDataDomain();
//        Sort sort = Sort.by(Sort.Direction.DESC,"id");
//        Pageable page = PageRequest.of(pageNumber,pageSize,sort);
//        Page<Organization> organizationPage = organizationRepository.findAll(page);
//
//        //当前页
//        pageDataDomain.setCurrent(pageNumber);
//        //总页数
//        pageDataDomain.setPages(pageSize);
//        //总条数
//        pageDataDomain.setTotal(organizationPage.getTotalElements());
//        organizationPage.getContent().forEach(organization -> {
//            OrganizationDomainV2 organizationDomainV2 =  new OrganizationDomainV2();
//            BeanUtils.copyProperties(organization,organizationDomainV2);
//            pageDataDomain.getRecords().add(organizationDomainV2);
//        });
        return pageDataDomain;
    }

    /**
     * 删除
     * @param organizationId
     */
    @Override
    @Transactional
    public void delete(Long organizationId){
        organizationRepository.deleteById(organizationId);
        OrganizationServiceImpl organizationService = (OrganizationServiceImpl) AopContext.currentProxy();
        organizationService.deleteDepartment(organizationId);

    }

    @Async
    @Transactional
    public void deleteDepartment(Long organizationId){
        //根据组织机构id删除所有部门信息
        departmentRepository.deleteAllByOrganizationId(organizationId);
        //在关联关系表中根据组织机构id查询所有userId
        List<UserDepartment> userDepartmentList = userDepartmentRepository.findByOrganizationId(organizationId);
        //去除重复
        Set<Long> userIds = new HashSet<>();
        //遍历删除用户信息
        userDepartmentList.forEach(userDepartment -> {
            userDepartmentRepository.delete(userDepartment);
            userIds.add(userDepartment.getUserId());
        });
        if (!userIds.isEmpty()){
            userRepository.deleteAllByIdIn(userIds);
        }
    }
}
