package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.common.exception.ExceptionValidation;
import cn.cnyaoshun.oauth.dao.DepartmentRepository;
import cn.cnyaoshun.oauth.dao.UserDepartmentRepository;
import cn.cnyaoshun.oauth.dao.UserRepository;
import cn.cnyaoshun.oauth.domain.DepartmentDomain;
import cn.cnyaoshun.oauth.domain.DepartmentDomainV2;
import cn.cnyaoshun.oauth.domain.DepartmentDomainV3;
import cn.cnyaoshun.oauth.entity.Department;
import cn.cnyaoshun.oauth.entity.User;
import cn.cnyaoshun.oauth.entity.UserDepartment;
import cn.cnyaoshun.oauth.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by fyh on 2020-6-12.
 */
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final UserDepartmentRepository userDepartmentRepository;

    private final UserRepository userRepository;

    /**
     * 根据机构id获取部门树结构
     * @param organizationId
     * @return
     */
    @Override
    public List<DepartmentDomain> findByOrganizationId(Long organizationId) {
        List<Department> departmentList = departmentRepository.findByOrganizationIdOrderBySort(organizationId);
        List<DepartmentDomain> departmentDomainList = new ArrayList<>();
        Map<Long, List<Department>> departmentMap = new HashMap<>();
        departmentList.forEach(department -> {
            if (department.getParentId() == null) {
                DepartmentDomain departmentDomain = new DepartmentDomain();
                BeanUtils.copyProperties(department, departmentDomain);
                departmentDomainList.add(departmentDomain);
            } else {
                List<Department> departments = departmentMap.get(department.getParentId());
                if (departments == null) {
                    departments = new ArrayList<>();
                }
                departments.add(department);
                departmentMap.put(department.getParentId(), departments);
            }
        });
        departmentDomainList.forEach(departmentDomain -> {
            recursiveDepartment(departmentDomain,departmentMap);
        });

        return departmentDomainList;
    }

    /**
     * 新增部门
     * @param departmentDomainV2
     * @return
     */
    @Override
    public Long insertDepartment(DepartmentDomainV2 departmentDomainV2) {
        if(departmentDomainV2.getParentId() == 0){
            throw  new ExceptionValidation(418,"父节点Id不能为0");
        }
        boolean depNumber = departmentRepository.existsByDepartmentNumber(departmentDomainV2.getDepartmentNumber());
        boolean organizationIdExt = departmentRepository.existsByOrganizationId(departmentDomainV2.getOrganizationId());
        if(depNumber){
            throw new ExceptionValidation(418,"部门编号已存在");
        }
        if(!organizationIdExt){
            throw new ExceptionValidation(418,"公司不存在请重新输入");
        }
        Department department = new Department();
        BeanUtils.copyProperties(departmentDomainV2, department);
        department.setState(true);
        //department.setSort(0);
        departmentRepository.save(department);
        return department.getId();
    }

    @Override
    @Transactional
    public void deleteDepartment(Long departmentId) {
        departmentRepository.deleteById(departmentId);
        DepartmentServiceImpl departmentService = (DepartmentServiceImpl) AopContext.currentProxy();
        departmentService.deleteUser(departmentId);
    }

    @Override
    public Long updateDepartment(DepartmentDomainV3 departmentDomainV3) {
        Optional<Department> departmentOptional = departmentRepository.findById(departmentDomainV3.getId());
        departmentOptional.ifPresent(department -> {
            BeanUtils.copyProperties(departmentDomainV3,department);
            department.setUpdateTime(new Date());
            department.setId(department.getId());
            departmentRepository.save(department);
        });
        return departmentDomainV3.getId();
    }

    @Async
    @Transactional
    public void  deleteUser(Long departmentId){
        List<Long> userIdList = userDepartmentRepository.findByUserId(departmentId);
        userIdList.forEach(userId ->{
            userRepository.deleteById(userId);
        });
    }

    /**
     * 递归组装部门树结构
     * @param departmentDomain
     * @param departmentMap
     */

    private void recursiveDepartment(DepartmentDomain departmentDomain,Map<Long, List<Department>> departmentMap){
        List<Department> departmentList = departmentMap.get(departmentDomain.getId());
        Optional.ofNullable(departmentList).ifPresent(departments -> departments.forEach(department -> {
            DepartmentDomain departDomain = new DepartmentDomain();
            BeanUtils.copyProperties(department, departDomain);
            departmentDomain.getChildren().add(departDomain);
            recursiveDepartment(departDomain,departmentMap);
        }));
    }
}
