package cn.cnyaoshun.oauth.dao;

import cn.cnyaoshun.oauth.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by fyh on 2020/6/17.
 */
public interface PermissionRepository extends JpaRepository<Permission , Long>,JpaSpecificationExecutor<Permission> {
    boolean existsByProjectIdAndPermissionNameAndIdNot(Long projectId,String permissionName,Long id);
    boolean existsByProjectIdAndPermissionTypeAndIdNot(Long projectId, String permissionType,Long id);
    boolean existsByProjectIdAndPermissionName(Long projectId,String permissionName);
    boolean existsByProjectIdAndPermissionType(Long projectId, String permissionType);
    List<Permission> findByProjectId(Long projectId);
}
