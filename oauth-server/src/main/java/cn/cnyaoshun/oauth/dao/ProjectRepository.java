package cn.cnyaoshun.oauth.dao;

import cn.cnyaoshun.oauth.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjectRepository extends JpaRepository<Project,Long> {
}
