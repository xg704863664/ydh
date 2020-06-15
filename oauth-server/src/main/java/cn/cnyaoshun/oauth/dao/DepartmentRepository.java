package cn.cnyaoshun.oauth.dao;

import cn.cnyaoshun.oauth.entity.Department;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by fyh on 2020-6-12.
 */
public interface DepartmentRepository extends PagingAndSortingRepository<Department,Long>{
    void deleteAllByOrganizationId(Long organizationId);
    List<Department> findByOrganizationIdOrderBySort(Long organizationId);
    boolean existsByDepartmentNo(String departmentNo);
    //boolean existsByOrganizationId(Long organizationId);
    Integer countByOrganizationIdAndParentIdIsNull(Long organizationId);
    Integer countByOrganizationIdAndParentId(Long organizationId, Long parentId);
    List<Department> findByParentIdAndIdNotInAndSortGreaterThan(Long parentId, Long department, Integer sort);

}
