package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.common.exception.ExceptionValidation;
import cn.cnyaoshun.oauth.dao.*;
import cn.cnyaoshun.oauth.domain.DepartmentTreeDomain;
import cn.cnyaoshun.oauth.domain.DepartmentAddDomain;
import cn.cnyaoshun.oauth.domain.DepartmentUpdateDomain;
import cn.cnyaoshun.oauth.entity.Account;
import cn.cnyaoshun.oauth.entity.Department;
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
 * @ClassName DepartmentServiceImpl
 * @Description 部门service实现类
 * @Author fyh
 * Date 2020-6-1516:56
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final OrganizationRepository organizationRepository;

    private final UserDepartmentRepository userDepartmentRepository;

    private final UserRepository userRepository;

    private  final AccountRepository accountRepository;

    private final AccountRoleRepository accountRoleRepository;

    /**
     * 根据机构id获取部门树结构
     * @param organizationId
     * @return
     */
    @Override
    public List<DepartmentTreeDomain> findByOrganizationId(Long organizationId) {
        List<Department> departmentList = departmentRepository.findByOrganizationIdOrderByIdDesc(organizationId);
        List<DepartmentTreeDomain> departmentTreeDomainList = new ArrayList<>();
        Map<Long, List<Department>> departmentMap = new HashMap<>();
        departmentList.forEach(department -> {
            if (department.getParentId() == null) {
                DepartmentTreeDomain departmentTreeDomain = new DepartmentTreeDomain();
                BeanUtils.copyProperties(department, departmentTreeDomain);
                departmentTreeDomainList.add(departmentTreeDomain);
            } else {
                List<Department> departments = departmentMap.get(department.getParentId());
                if (departments == null) {
                    departments = new ArrayList<>();
                }
                departments.add(department);
                departmentMap.put(department.getParentId(), departments);
            }
        });
        departmentTreeDomainList.forEach(departmentTreeDomain -> recursiveDepartment(departmentTreeDomain,departmentMap));
        return departmentTreeDomainList;
    }

    /**
     * 新增部门
     * @param departmentAddDomain
     * @return
     */
    @Transactional
    @Override
    public Long add(DepartmentAddDomain departmentAddDomain) {

        if(departmentAddDomain.getParentId()!=null &&  departmentAddDomain.getParentId() == 0){
            throw  new ExceptionValidation(418,"父节点ID不能为0");
        }
        boolean depNumber = departmentRepository.existsByDepartmentNo(departmentAddDomain.getDepartmentNo());
        if(depNumber){
            throw new ExceptionValidation(418,"部门编号已存在");
        }
        boolean organizationIdExt = organizationRepository.existsById(departmentAddDomain.getOrganizationId());
        if(!organizationIdExt){
            throw new ExceptionValidation(418,"公司不存在请重新输入");
        }
        Department department = new Department();
        BeanUtils.copyProperties(departmentAddDomain, department);
        departmentRepository.save(department);
        return department.getId();
    }

    /**
     * 删除部门
     * @param departmentId
     */
    @Override
    @Transactional
    public void delete(Long departmentId) {

        Optional<Department> departmentOptional = departmentRepository.findById(departmentId);

        departmentOptional.ifPresent((department ) -> {

            List<Department> departmentList = departmentRepository.findByParentIdAndIdNotIn(department.getParentId(),departmentId);
            departmentList.forEach(department1 -> {
                    departmentRepository.save(department1);
            });
        });
        departmentRepository.deleteById(departmentId);
        log.info("部门删除成功,删除的部门ID为:"+departmentId);
        DepartmentServiceImpl departmentService = (DepartmentServiceImpl) AopContext.currentProxy();
        departmentService.deleteUser(departmentId);
    }

    @Override
    @Transactional
    public Long update(DepartmentUpdateDomain departmentUpdateDomain) {

        Optional<Department> departmentOptional = departmentRepository.findById(departmentUpdateDomain.getDepartmentId());
        departmentOptional.ifPresent(department -> {
           department.setId(department.getId());
           department.setDepartmentName(departmentUpdateDomain.getDepartmentName());
           department.setUpdateTime(new Date());
           departmentRepository.save(department);
       });
        return departmentUpdateDomain.getDepartmentId();
    }

    @Async
    @Transactional
    public void  deleteUser(Long departmentId){

        List<Long> userIdList = userDepartmentRepository.findByUserId(departmentId);
        userIdList.forEach(userId ->{
            userRepository.deleteById(userId);
            accountRepository.deleteAllByUserId(userId);
            List<Account> accountList = accountRepository.findByUserId(userId);
            accountList.forEach(account -> {
                accountRoleRepository.deleteAllByAccountId(account.getId());
            });
        });
        log.info("部门关联信息删除成功");
    }

    /**
     * 递归组装部门树结构
     * @param departmentTreeDomain
     * @param departmentMap
     */
    private void recursiveDepartment(DepartmentTreeDomain departmentTreeDomain, Map<Long, List<Department>> departmentMap){

        List<Department> departmentList = departmentMap.get(departmentTreeDomain.getId());

        Optional.ofNullable(departmentList).ifPresent(departments -> departments.forEach(department -> {
            DepartmentTreeDomain departDomain = new DepartmentTreeDomain();
            BeanUtils.copyProperties(department, departDomain);
            departmentTreeDomain.getChildren().add(departDomain);
            recursiveDepartment(departDomain,departmentMap);
        }));
    }
}
