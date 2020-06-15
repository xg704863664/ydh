package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.common.exception.ExceptionValidation;
import cn.cnyaoshun.oauth.dao.*;
import cn.cnyaoshun.oauth.domain.DepartmentDomain;
import cn.cnyaoshun.oauth.domain.DepartmentDomainV2;
import cn.cnyaoshun.oauth.domain.DepartmentDomainV3;
import cn.cnyaoshun.oauth.entity.Department;
import cn.cnyaoshun.oauth.service.DepartmentService;
import lombok.RequiredArgsConstructor;
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

    private final OrganizationRepository organizationRepository;

    private final UserDepartmentRepository userDepartmentRepository;

    private final UserRepository userRepository;

    private  final AccountRepository accountRepository;

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
    public Long add(DepartmentDomainV2 departmentDomainV2) {

        if(departmentDomainV2.getParentId()!=null && departmentDomainV2.getParentId() == 0){
            throw  new ExceptionValidation(418,"父节点ID不能为0");
        }
        boolean depNumber = departmentRepository.existsByDepartmentNo(departmentDomainV2.getDepartmentNo());
        if(depNumber){
            throw new ExceptionValidation(418,"部门编号已存在");
        }
        //boolean organizationIdExt = departmentRepository.existsByOrganizationId(departmentDomainV2.getOrganizationId());
        boolean organizationIdExt = organizationRepository.existsById(departmentDomainV2.getOrganizationId());
        if(!organizationIdExt){
            throw new ExceptionValidation(418,"公司不存在请重新输入");
        }
        Integer count = null;
        if (departmentDomainV2.getParentId() == null){
            count = departmentRepository.countByOrganizationIdAndParentIdIsNull(departmentDomainV2.getOrganizationId());
        }else {
            count = departmentRepository.countByOrganizationIdAndParentId(departmentDomainV2.getOrganizationId(),departmentDomainV2.getParentId());
        }
        Department department = new Department();
        BeanUtils.copyProperties(departmentDomainV2, department);
        department.setState(true);
        department.setSort(count == 0L || count == null ? 1 : count + 1);
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

            Integer departmentSort = department.getSort();
            List<Department> departmentList = departmentRepository.findByParentIdAndIdNotInAndSortGreaterThan(department.getParentId(),departmentId,departmentSort);
            departmentList.forEach(department1 -> {
                if(department1.getSort() > departmentSort){
                    department1.setSort(department1.getSort()-1);
                    departmentRepository.save(department1);
                }
            });
        });
        departmentRepository.deleteById(departmentId);
        DepartmentServiceImpl departmentService = (DepartmentServiceImpl) AopContext.currentProxy();
        departmentService.deleteUser(departmentId);
    }

    @Override
    @Transactional
    public Long update(DepartmentDomainV3 departmentDomainV3) {

        Optional<Department> departmentOptional = departmentRepository.findById(departmentDomainV3.getDepartmentId());
       departmentOptional.ifPresent(department -> {
           department.setDepartmentName(departmentDomainV3.getDepartmentName());
           departmentRepository.save(department);
       });
        return departmentDomainV3.getDepartmentId();
    }

    @Async
    @Transactional
    public void  deleteUser(Long departmentId){

        List<Long> userIdList = userDepartmentRepository.findByUserId(departmentId);
        userIdList.forEach(userId ->{
            userRepository.deleteById(userId);
            accountRepository.deleteAllByUserId(userId);
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
