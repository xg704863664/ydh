package cn.cnyaoshun.form.organization.repository;

import cn.cnyaoshun.form.organization.model.Organization;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @Author:
 * @Date: 2020/7/8 11:26
 */
public interface OrganizationRepository extends CrudRepository<Organization,Long> {
      List<Organization> findAll();
}
