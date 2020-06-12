package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.dao.DepartmentRepository;
import cn.cnyaoshun.oauth.domain.DepartmentDomain;
import cn.cnyaoshun.oauth.entity.Department;
import cn.cnyaoshun.oauth.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fyh on 2020-6-12.
 */
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public Map<Long ,List<DepartmentDomain>> findByOrganizationId(Long organizationId) {

        List<Department> departmentList = departmentRepository.findByOrganizationIdOrderBySort(organizationId);

        List<DepartmentDomain> departmentDomain = new ArrayList<DepartmentDomain>();
        departmentList.forEach(department -> {
            if(department.getParentId()==null){

            }
        });
        Map<Long,List<DepartmentDomain>> organizationTree = new HashMap<>();
        organizationTree.put(organizationId , departmentDomain);
        return organizationTree;
    }
}
