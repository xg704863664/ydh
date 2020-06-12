package cn.cnyaoshun.oauth.dao;

import cn.cnyaoshun.oauth.entity.Organization;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by fyh on 2020-6-11.
 */
public interface OrganizationRepository extends JpaRepository<Organization,Long> {

}
