package cn.cnyaoshun.oauth.dao;

import cn.cnyaoshun.oauth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by fyh on 2020-6-15.
 */
public interface RoleRepository extends JpaRepository<Role, Long>,JpaSpecificationExecutor<Role> {
    List<Role> findByProjectId(Long projectId);
    boolean existsByProjectIdAndAndRoleName(Long projectId, String roleName);
}
