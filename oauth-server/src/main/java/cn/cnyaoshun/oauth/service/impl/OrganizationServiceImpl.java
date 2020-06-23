package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.dao.*;
import cn.cnyaoshun.oauth.domain.OrganizationAddDomain;
import cn.cnyaoshun.oauth.domain.OrganizationUpdateDomain;
import cn.cnyaoshun.oauth.domain.OrganizationFindAllDomain;
import cn.cnyaoshun.oauth.entity.Account;
import cn.cnyaoshun.oauth.entity.Organization;
import cn.cnyaoshun.oauth.entity.UserDepartment;
import cn.cnyaoshun.oauth.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @ClassName OrganizationServiceImpl
 * @Description 组织机构实现类
 * @Author fyh
 * Date 2020/6/1110:11
 */
@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService{

    private final OrganizationRepository organizationRepository;

    private final DepartmentRepository departmentRepository;

    private final UserDepartmentRepository userDepartmentRepository;

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    private final AccountRoleRepository accountRoleRepository;
    /**
     * 新增
     * @param organizationAddDomain
     * @return
     */
    @Override
    @Transactional
    public Long add(OrganizationAddDomain organizationAddDomain){
        Organization organization = new Organization();
        BeanUtils.copyProperties(organizationAddDomain, organization);
        Organization SaveOrganization = organizationRepository.save(organization);
        return SaveOrganization.getId();
    }

    /**
     * 修改
     * @param organizationUpdateDomain
     * @return
     */
    @Override
    @Transactional
    public Long update(OrganizationUpdateDomain organizationUpdateDomain){
        Optional<Organization> organizationOptional = organizationRepository.findById(organizationUpdateDomain.getId());
        //满足条件时(organizationOptional为true时)进入下面条件
        organizationOptional.ifPresent(organization -> {
            BeanUtils.copyProperties(organizationUpdateDomain,organization);
            organization.setId(organization.getId());
            organization.setUpdateTime(new Date());
            organizationRepository.save(organization);
        });
        return organizationUpdateDomain.getId();
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

    /**
     * 查询
     * @return
     */
    @Override
    public List<OrganizationFindAllDomain> findAll(){

        List<Organization> organizationList = organizationRepository.findAll();
        List<OrganizationFindAllDomain> organizationDaomainList = new ArrayList<>();
        organizationList.forEach(organization -> {
            OrganizationFindAllDomain organizationFindAllDomain = new OrganizationFindAllDomain();
            organizationFindAllDomain.setId(organization.getId());
            organizationFindAllDomain.setOrganizationName(organization.getOrganizationName());
            organizationFindAllDomain.setAddress(organization.getAddress());
            organizationFindAllDomain.setType(1);
            organizationDaomainList.add(organizationFindAllDomain);
        });
        return organizationDaomainList;
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
            //根据用户信息删除账户信息
            List<Account> accountList = new ArrayList<>();
            userIds.forEach(userId ->{
                List<Account> byUserId = accountRepository.findByUserId(userId);
                accountList.addAll(byUserId);
            });
            if(!accountList.isEmpty()){
                accountList.forEach(account -> {
                    accountRepository.deleteAllByIdIn(account.getId());
                    accountRoleRepository.deleteAllByAccountId(account.getId());
                });
            }
        }
    }
}
