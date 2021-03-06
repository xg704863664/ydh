package cn.cnyaoshun.oauth.dao;

import cn.cnyaoshun.oauth.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by fyh on 2020-6-11.
 */
public interface OrganizationRepository extends JpaRepository<Organization,Long> {
    boolean existsById(Long organizationId);
    boolean existsByOrganizationName(String organizationName);
    boolean existsByOrganizationNameAndIdNot(String organizationName,long id);
    Organization findByOrganizationName(String orgName);
}
