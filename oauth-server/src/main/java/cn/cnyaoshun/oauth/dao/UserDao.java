package cn.cnyaoshun.oauth.dao;

import cn.cnyaoshun.oauth.domain.UserFindAllByDepartmentIdDomain;
import cn.cnyaoshun.oauth.domain.UserFindAllByOrgDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fyh on 2020-6-5.
 */
@Repository
@RequiredArgsConstructor
public class UserDao {

    private final EntityManager entityManager;

    public List<UserFindAllByDepartmentIdDomain> findUserByDepartmentId(Long departmentId, String keyWord, Integer pageNumber, Integer pageSize){
        List<UserFindAllByDepartmentIdDomain> userDomainList = new ArrayList<>();
        StringBuilder sql =  new StringBuilder("SELECT cu.id,cu.user_name,cu.sex,cu.age,cu.phone,cu.email,cu.id_no,cu.create_time,cu.update_time, ud.department_id FROM user AS cu, user_department AS ud  WHERE  ud.department_id = ? AND cu.id = ud.user_id ");
        if (!StringUtils.isEmpty(keyWord)){
            sql.append(" and cu.user_name like '%"+ keyWord +"%'");
        }
        sql.append(" ORDER BY ud.id DESC LIMIT ?,?");
        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        nativeQuery.setParameter(1,departmentId);
        nativeQuery.setParameter(2,pageNumber);
        nativeQuery.setParameter(3,pageSize);
        List<Object[]> objects =  nativeQuery.getResultList();
        //拿到类所有字段,通过java反射机制
        Field[] declaredFields = UserFindAllByDepartmentIdDomain.class.getDeclaredFields();
        objects.forEach(array -> {
            UserFindAllByDepartmentIdDomain userDomain = new UserFindAllByDepartmentIdDomain();
            for (int i = 0; i < array.length; i++) {
                try {
                    declaredFields[i].setAccessible(true);
                    if (array[i] instanceof BigInteger) {
                        declaredFields[i].set(userDomain, Long.valueOf(array[i].toString()));
                    }else {
                        if(declaredFields[i].getName().equals("state")){
                            declaredFields[i].set(userDomain, Integer.parseInt(array[i].toString())==1?true:false);
                        }else {
                            declaredFields[i].set(userDomain, array[i]);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            userDomainList.add(userDomain);
        });
        return userDomainList;
    }

    public Long countUserEntitiesByDepartmentId(Long organizationId, String keyWord){
        StringBuilder sql =  new StringBuilder("SELECT COUNT(1) FROM user AS cu, user_department AS ud  WHERE  ud.department_id = ? AND cu.id = ud.user_id ");
        if (!StringUtils.isEmpty(keyWord)){
            sql.append(" and cu.user_name like '%"+keyWord+"%'" );
        }
        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        nativeQuery.setParameter(1,organizationId);
        String count = nativeQuery.getSingleResult().toString();
        return Long.valueOf(count);
    }

    public List<UserFindAllByOrgDomain> userFindAllByOrgDomain(Long organizationId, String keyWord, Integer pageNumber, Integer pageSize){
        List<UserFindAllByOrgDomain> userFindAllByOrgDomains = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT de.department_name,u.id,u.user_name,u.sex ,u.age,u.phone,u.email,u.id_no FROM user_department AS ud  LEFT JOIN department AS de ON de.id = ud.department_id LEFT JOIN  `user` AS u  on ud.user_id = u.id WHERE ud.organization_id = ? ");
        if(!StringUtils.isEmpty(keyWord)){
            sql.append(" and u.user_name like '%"+keyWord+"%' OR de.department_name like '%"+keyWord+ "%'" );
        }
        sql.append(" ORDER BY u.id DESC LIMIT ?,?");
        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        nativeQuery.setParameter(1,organizationId);
        nativeQuery.setParameter(2,pageNumber);
        nativeQuery.setParameter(3,pageSize);
        List<Object[]> objects =  nativeQuery.getResultList();
        Field[] declaredFields = UserFindAllByOrgDomain.class.getDeclaredFields();

        objects.forEach(array -> {
            UserFindAllByOrgDomain userDomain = new UserFindAllByOrgDomain();
                    try {
                        declaredFields[0].setAccessible(true);
                        declaredFields[0].set(userDomain, array[0].toString());
                        declaredFields[1].setAccessible(true);
                        declaredFields[1].set(userDomain, Long.valueOf(array[1].toString()));
                        declaredFields[2].setAccessible(true);
                        declaredFields[2].set(userDomain, array[2].toString());
                        declaredFields[3].setAccessible(true);
                        declaredFields[3].set(userDomain, array[3].toString());
                        declaredFields[4].setAccessible(true);
                        declaredFields[4].set(userDomain, Integer.valueOf(array[4].toString()));
                        declaredFields[5].setAccessible(true);
                        declaredFields[5].set(userDomain, array[5].toString());
                        if (array[6] != null) {
                            declaredFields[6].setAccessible(true);
                            declaredFields[6].set(userDomain, array[6].toString());
                        }
                        if(array[7] != null){
                            declaredFields[7].setAccessible(true);
                            declaredFields[7].set(userDomain, array[7].toString());
                        }
                    }catch(IllegalAccessException e) {
                        e.fillInStackTrace();
                    }
                userFindAllByOrgDomains.add(userDomain);
        });
        return  userFindAllByOrgDomains;
    }

    public Long countUserNumber(Long organizationId, String keyWord){
        StringBuilder sql = new StringBuilder("SELECT count(1) FROM user_department AS ud  LEFT JOIN department AS de ON de.id = ud.department_id LEFT JOIN  `user` AS u  on ud.user_id = u.id WHERE ud.organization_id = ? ");
        if(!StringUtils.isEmpty(keyWord)){
            sql.append(" and u.user_name like '%"+keyWord+"%' OR de.department_name like '%"+ keyWord+ "%'" );
        }
        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        nativeQuery.setParameter(1,organizationId);
        String count = nativeQuery.getSingleResult().toString();
        return  Long.valueOf(count);
    }

}
