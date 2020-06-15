package cn.cnyaoshun.oauth.dao;

import cn.cnyaoshun.oauth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by fyh on 2020-6-15.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
}
