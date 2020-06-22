package cn.cnyaoshun.oauth.dao;


import cn.cnyaoshun.oauth.domain.UserDomainV5;
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

    public List<UserDomainV5> findUserByDepartmentId(Long departmentId, String keyWord, Integer pageNumber, Integer pageSize){
        List<UserDomainV5> userDomainList = new ArrayList<>();
        StringBuilder sql =  new StringBuilder("SELECT cu.id,cu.user_name,cu.sex,cu.age,cu.phone,cu.email,cu.id_no,cu.create_time,cu.update_time, ud.department_id FROM user AS cu, user_department AS ud  WHERE  ud.department_id = ? AND cu.id = ud.user_id ");
        if (!StringUtils.isEmpty(keyWord)){
            sql.append(" and cu.user_name like '%"+ keyWord +"%'or cu.user_no like '%" + keyWord +"%'");
        }
        sql.append(" ORDER BY ud.id DESC LIMIT ?,?");
        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        nativeQuery.setParameter(1,departmentId);
        nativeQuery.setParameter(2,pageNumber);
        nativeQuery.setParameter(3,pageSize);
        List<Object[]> objects =  nativeQuery.getResultList();
        //拿到类所有字段,通过java反射机制
        Field[] declaredFields = UserDomainV5.class.getDeclaredFields();
        objects.forEach(array -> {
            UserDomainV5 userDomain = new UserDomainV5();
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
            sql.append(" and cu.user_name like '%"+keyWord+"%'or cu.user_no like '%" + keyWord +"%'" );
        }
        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        nativeQuery.setParameter(1,organizationId);
        String count = nativeQuery.getSingleResult().toString();
        return Long.valueOf(count);
    }
}
