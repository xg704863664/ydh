package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.dao.DepartmentRepository;
import cn.cnyaoshun.oauth.domain.DepartmentDomain;
import cn.cnyaoshun.oauth.entity.Department;
import cn.cnyaoshun.oauth.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by fyh on 2020-6-12.
 */
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

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
