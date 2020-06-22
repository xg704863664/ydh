package cn.cnyaoshun.oauth.dao;

import cn.cnyaoshun.oauth.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by fyh on 2020/6/17.
 */
public interface PermissionRepository extends JpaRepository<Permission , Long> {

    boolean existsByPermissionName(String permissionName);

    List<Permission> findByProjectId(Long projectId);
}
