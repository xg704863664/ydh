package cn.cnyaoshun.oauth.dao;

import cn.cnyaoshun.oauth.domain.AccountDomainV2;
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
 * @ClassName AccountDao
 * @Description DOTO
 * @Author fyh
 * Date 2020-6-1518:06
 */
@Repository
@RequiredArgsConstructor
public class AccountDao {

    private  final EntityManager entityManager;

    public List<AccountDomainV2> findAllByRoleId(Long roleId,String keyWord, Integer pageNumber, Integer pageSize){
        List<AccountDomainV2> accountDomainV2List = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT a.id ,a.account_name,u.user_name,a.state from account_role as ar left JOIN account as a on ar.account_id=a.id LEFT JOIN `user` as u on u.id=a.user_id where ar.role_id=? ");
        if (!StringUtils.isEmpty(keyWord)) {
            sql.append(" and (a.account_name like '%"+keyWord+"%' OR u.user_name LIKE '%"+keyWord+"%')");
        }
        sql.append(" ORDER BY ar.id DESC limit ?,?");
        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        nativeQuery.setParameter(1,roleId);
        nativeQuery.setParameter(2,pageNumber);
        nativeQuery.setParameter(3,pageSize);
        List<Object[]> objects =  nativeQuery.getResultList();
        Field[] declaredFields = AccountDomainV2.class.getDeclaredFields();
        objects.forEach(object -> {
            AccountDomainV2 accountDomainV2 = new AccountDomainV2();
            try {
                declaredFields[0].setAccessible(true);
                declaredFields[0].set(accountDomainV2,Long.valueOf(object[0].toString()));
                declaredFields[1].setAccessible(true);
                declaredFields[1].set(accountDomainV2,object[1].toString());
                declaredFields[2].setAccessible(true);
                declaredFields[2].set(accountDomainV2,object[2].toString());
                declaredFields[3].setAccessible(true);
                declaredFields[3].set(accountDomainV2,Long.valueOf(object[3].toString())==1?true:false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
                accountDomainV2List.add(accountDomainV2);
            });
        return accountDomainV2List;
    }

    public Long countAccountByRoleId(Long roleId, String keyWord){

        StringBuilder sql = new StringBuilder("SELECT count(1) from account_role as ar left JOIN account as a on ar.account_id=a.id LEFT JOIN `user` as u on u.id=a.user_id where ar.role_id=? ");
        if (!StringUtils.isEmpty(keyWord)) {
            sql.append(" and (a.account_name like '%"+keyWord+"%' OR u.user_name LIKE '%"+keyWord+"%')");
        }
        Query nativeQuery = entityManager.createNativeQuery(sql.toString());
        nativeQuery.setParameter(1,roleId);
        String count = nativeQuery.getSingleResult().toString();
        return Long.valueOf(count.toString());
    }
}
