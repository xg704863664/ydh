package cn.cnyaoshun.oauth.dao;

import cn.cnyaoshun.oauth.entity.Department;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Set;

/**
 * Created by fyh on 2020-6-12.
 */
public interface DepartmentRepository extends PagingAndSortingRepository<Department,Long>{
    void deleteAllByOrganizationId(Long organizationId);
    List<Department> findByOrganizationIdOrderBySort(Long organizationId);
}
