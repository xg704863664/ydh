package cn.cnyaoshun.oauth.dao;

import cn.cnyaoshun.oauth.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by fyh on 2020/6/17.
 */
public interface PermissionRepository extends JpaRepository<Permission , Long>,JpaSpecificationExecutor<Permission> {
    boolean existsByPermissionName(String permissionName);
    List<Permission> findByProjectId(Long projectId);
}
