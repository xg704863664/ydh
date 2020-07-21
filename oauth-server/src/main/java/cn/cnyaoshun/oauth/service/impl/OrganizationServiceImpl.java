package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.common.exception.ExceptionValidation;
import cn.cnyaoshun.oauth.dao.*;
import cn.cnyaoshun.oauth.domain.OrganizationAddDomain;
import cn.cnyaoshun.oauth.domain.OrganizationFindAllDomain;
import cn.cnyaoshun.oauth.domain.OrganizationUpdateDomain;
import cn.cnyaoshun.oauth.entity.Organization;
import cn.cnyaoshun.oauth.entity.UserDepartment;
import cn.cnyaoshun.oauth.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName OrganizationServiceImpl
 * @Description 组织机构实现类
 * @Author fyh
 * Date 2020/6/1110:11
 */
@Service
@RequiredArgsConstructor
@Slf4j
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
        boolean existsByOrganizationName = organizationRepository.existsByOrganizationName(organizationAddDomain.getOrganizationName());
        if(existsByOrganizationName){
            throw new ExceptionValidation(418,"组织机构已存在,请重新输入");
        }
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
        boolean organizationUpdate = organizationRepository.existsByOrganizationNameAndIdNot(organizationUpdateDomain.getOrganizationName(), organizationUpdateDomain.getId());
        if(organizationUpdate){
            throw new ExceptionValidation(418,"组织机构已存在,请重新输入");
        }
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
        log.info("组织机构信息及其关联信息删除成功,删除的组织机构ID为"+organizationId);
    }

    /**
     * 查询
     * @return
     */
    @Override
    public List<OrganizationFindAllDomain> findAll(){

        List<Organization> organizationList = organizationRepository.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
        List<OrganizationFindAllDomain> organizationDaomainList = new ArrayList<>();
        organizationList.forEach(organization -> {
            OrganizationFindAllDomain organizationFindAllDomain = new OrganizationFindAllDomain();
            organizationFindAllDomain.setId(organization.getId());
            organizationFindAllDomain.setOrganizationName(organization.getOrganizationName());
            organizationFindAllDomain.setAddress(organization.getAddress());
            organizationFindAllDomain.setOrganizationPhone(organization.getOrganizationPhone());
            organizationFindAllDomain.setType(1);
            organizationDaomainList.add(organizationFindAllDomain);
        });
        log.info("组织机构信息获取成功.共有"+organizationDaomainList.size()+"条数据");
        return organizationDaomainList;
    }

    @Async
    @Transactional
    public void deleteDepartment(Long organizationId){
        //根据组织机构id删除所有部门信息
        departmentRepository.deleteAllByOrganizationId(organizationId);
        //在关联关系表中根据组织机构id查询所有userId
        List<UserDepartment> userDepartmentList = userDepartmentRepository.findByOrganizationId(organizationId);
        Optional.ofNullable(userDepartmentList).ifPresent(userDepartments -> userDepartmentRepository.deleteAll(userDepartments));
        Set<Long> userIds =userDepartmentList.stream().map(UserDepartment::getUserId).collect(Collectors.toSet());
        Optional.ofNullable(userIds).ifPresent(uis -> {
            userRepository.deleteAllByIdIn(uis);
            uis.stream().map(userId -> accountRepository.findByUserId(userId)).forEach(accounts -> accounts.stream().forEach(account -> {
                accountRepository.deleteAllByIdIn(account.getId());
                accountRoleRepository.deleteAllByAccountId(account.getId());
            }
          ));
        });
    }
}
