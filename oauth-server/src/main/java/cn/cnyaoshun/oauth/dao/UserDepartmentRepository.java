package cn.cnyaoshun.oauth.dao;

import cn.cnyaoshun.oauth.entity.UserDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDepartmentRepository extends JpaRepository<UserDepartment,Long> {
}
