package cn.cnyaoshun.oauth.dao;


import cn.cnyaoshun.oauth.domain.UserDomainV2;
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


    public List<UserDomainV2> findUserByDepartmentId(Integer pageNumber, Integer pageSize,Long departmentId, String name){
        List<UserDomainV2> userDomainList = new ArrayList<>();
        StringBuilder sql =  new StringBuilder("SELECT cu.id,cu.user_no,cu.user_name,cu.sex,cu.age,cu.phone,cu.email,cu.id_type,cu.id_no,cu.address,cu.state,cu.create_time,cu.update_time FROM user AS cu, user_department AS ud  WHERE  ud.department_id = ? AND cu.id = ud.user_id ");
        if (!StringUtils.isEmpty(name)){
            sql.append(" and cu.user_name like '%"+name+"%'");
        }
        sql.append(" ORDER BY ud.id DESC LIMIT ?,?");
        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        nativeQuery.setParameter(1,departmentId);
        nativeQuery.setParameter(2,pageNumber);
        nativeQuery.setParameter(3,pageSize);
        List<Object[]> objects =  nativeQuery.getResultList();
        //拿到类所有字段,通过java反射机制
        Field[] declaredFields = UserDomainV2.class.getDeclaredFields();
        objects.forEach(array -> {
            UserDomainV2 userDomain = new UserDomainV2();
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

    public Long countUserEntitiesByDepartmentId(Long organizationId, String name){
        StringBuilder sql =  new StringBuilder("SELECT COUNT(1) FROM user AS cu, user_department AS ud  WHERE  ud.department_id = ? AND cu.id = ud.user_id ");
        if (!StringUtils.isEmpty(name)){
            sql.append(" and cu.user_name like '%"+name+"%'");
        }
        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        nativeQuery.setParameter(1,organizationId);
        String count = nativeQuery.getSingleResult().toString();
        return Long.valueOf(count);
    }
}
