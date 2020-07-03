package cn.cnyaoshun.oauth.dao;

import cn.cnyaoshun.oauth.domain.PermissionOauthUserListDomain;
import cn.cnyaoshun.oauth.domain.RoleFindAllByOauthDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName OauthUserListDao
 * @Description
 * @Author fyh
 * Date 2020/6/1917:19
 */
@Repository
@RequiredArgsConstructor
public class OauthUserListDao {

    private final EntityManager entityManager;

    public List<RoleFindAllByOauthDomain> getAllRoleDomain(Long accountId, Long projectId){
        //角色信息
        StringBuilder roleSql = new StringBuilder("SELECT r.id,r.role_name from account_role as ar,role as r WHERE r.project_id=? and ar.account_id = ? and ar.role_id = r.id");
        Query nativeQuery = entityManager.createNativeQuery(roleSql.toString());
        nativeQuery.setParameter(1,projectId);
        nativeQuery.setParameter(2,accountId);
        List<RoleFindAllByOauthDomain> roleList = new ArrayList<>();
        List<Object[]> objects =  nativeQuery.getResultList();
        Field[] declaredFields = RoleFindAllByOauthDomain.class.getDeclaredFields();
        objects.forEach(object -> {
            RoleFindAllByOauthDomain roleFindAllByProjectIdAndAccountDomain = new RoleFindAllByOauthDomain();
            try {
                declaredFields[0].setAccessible(true);
                declaredFields[0].set(roleFindAllByProjectIdAndAccountDomain,Long.valueOf(object[0].toString()));
                declaredFields[1].setAccessible(true);
                declaredFields[1].set(roleFindAllByProjectIdAndAccountDomain,object[1].toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            roleList.add(roleFindAllByProjectIdAndAccountDomain);
        });
        return roleList;
    }

    public List<PermissionOauthUserListDomain> getAllPermissionList(List<Long> roleIdList){
        StringBuilder permissionListSql = new StringBuilder("SELECT p.id,p.permission_name,p.permission_type from role_permission as rp,permission as p where rp.role_id in(?) and rp.permission_id=p.id");
        Query nativeQuery = entityManager.createNativeQuery(permissionListSql.toString());
        String ids = roleIdList.stream().map(id -> String.valueOf(id)).collect(Collectors.joining(","));
        nativeQuery.setParameter(1,ids);
        List<PermissionOauthUserListDomain> permissionOauthUserListDomains = new ArrayList<>();
        List<Object[]> objects =  nativeQuery.getResultList();
        Field[] declaredFieldPermission = PermissionOauthUserListDomain.class.getDeclaredFields();
        objects.forEach(object -> {
            PermissionOauthUserListDomain permissionOauthUserListDomain = new PermissionOauthUserListDomain();
            try {
                declaredFieldPermission[0].setAccessible(true);
                declaredFieldPermission[0].set(permissionOauthUserListDomain,Long.valueOf(object[0].toString()));
                declaredFieldPermission[1].setAccessible(true);
                declaredFieldPermission[1].set(permissionOauthUserListDomain,object[1].toString());
                declaredFieldPermission[2].setAccessible(true);
                declaredFieldPermission[2].set(permissionOauthUserListDomain,object[2].toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            permissionOauthUserListDomains.add(permissionOauthUserListDomain);
        });
        return permissionOauthUserListDomains;
    }

}
