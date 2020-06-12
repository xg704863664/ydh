package cn.cnyaoshun.oauth.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Created by fyh on 2020-6-12.
 */
@Repository
@RequiredArgsConstructor
public class OrganizationDao {

    private final EntityManager entityManager;

    public void deleteDepartment(Long organizationId){
        StringBuilder sql = new StringBuilder("DELETE FROM department  WHERE department.organization_id IN (SELECT * FROM department WHERE department.organization_id = ? ");
        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        nativeQuery.setParameter(1,organizationId);
        String deleteDepmemnt = nativeQuery.getSingleResult().toString();
    }
     public void deleteUser(Long organization){
        
     }
}
