package cn.cnyaoshun.oauth.dao;

import cn.cnyaoshun.oauth.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by fyh on 2020/6/17.
 */
public interface RolePermissionRepository extends JpaRepository<RolePermission , Long> {
    List<RolePermission> findByRoleId(Long roleId);
}
