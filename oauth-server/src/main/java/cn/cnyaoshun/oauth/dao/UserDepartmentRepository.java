package cn.cnyaoshun.oauth.dao;

import cn.cnyaoshun.oauth.entity.UserDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDepartmentRepository extends JpaRepository<UserDepartment,Long> {
    List<UserDepartment> findByOrganizationId(Long organizationId);
}
