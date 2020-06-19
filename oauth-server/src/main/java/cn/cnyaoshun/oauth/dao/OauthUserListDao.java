package cn.cnyaoshun.oauth.dao;

import cn.cnyaoshun.oauth.domain.PermissionDomainOauthList;
import cn.cnyaoshun.oauth.domain.RoleDomainV3;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName OauthUserListDao
 * @Description DOTO
 * @Author fyh
 * Date 2020/6/1917:19
 */
@Repository
@RequiredArgsConstructor
public class OauthUserListDao {

    private final EntityManager entityManager;

    public List<RoleDomainV3> getAllRoleDomain(Long accountId, Long projectId){
        //角色信息
        StringBuilder roleSql = new StringBuilder("SELECT r.id,r.role_name from account_role as ar,role as r WHERE r.project_id=? and ar.account_id = ? and ar.role_id = r.id");
        Query nativeQuery = entityManager.createNativeQuery(roleSql.toString());
        nativeQuery.setParameter(1,projectId);
        nativeQuery.setParameter(2,accountId);
        List<RoleDomainV3> roleList = new ArrayList<>();
        List<Object[]> objects =  nativeQuery.getResultList();
        Field[] declaredFields = RoleDomainV3.class.getDeclaredFields();
        objects.forEach(object -> {
            RoleDomainV3 roleDomainV3 = new RoleDomainV3();
            try {
                declaredFields[0].setAccessible(true);
                declaredFields[0].set(roleDomainV3,Long.valueOf(object[0].toString()));
                declaredFields[1].setAccessible(true);
                declaredFields[1].set(roleDomainV3,object[1].toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            roleList.add(roleDomainV3);
        });
        return roleList;
    }

    public List<PermissionDomainOauthList> getAllPermissionList(List<Long> roleIdLits){
        StringBuilder permissionListSql = new StringBuilder("SELECT p.id,p.permission_name,p.permission_type from role_permission as rp,permission as p where rp.role_id in("+ roleIdLits +") and rp.permission_id=p.id");
        Query nativeQuery = entityManager.createNativeQuery(permissionListSql.toString());
        nativeQuery.setParameter(1,roleIdLits);
        List<PermissionDomainOauthList> permissionDomainOauthLists = new ArrayList<>();
        List<Object[]> objects =  nativeQuery.getResultList();
        Field[] declaredFieldPermission = PermissionDomainOauthList.class.getDeclaredFields();
        objects.forEach(object -> {
            PermissionDomainOauthList permissionDomainOauthList = new PermissionDomainOauthList();

            try {
                declaredFieldPermission[0].setAccessible(true);
                declaredFieldPermission[0].set(permissionDomainOauthList,Long.valueOf(object[0].toString()));
                declaredFieldPermission[1].setAccessible(true);
                declaredFieldPermission[1].set(permissionDomainOauthList,object[1].toString());
                declaredFieldPermission[2].setAccessible(true);
                declaredFieldPermission[2].set(permissionDomainOauthList,object[2].toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            permissionDomainOauthLists.add(permissionDomainOauthList);
        });
        return permissionDomainOauthLists;
    }

}
