package cn.cnyaoshun.oauth.dao;

import cn.cnyaoshun.oauth.domain.AccountFindAllByRoleIdDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AccountDao
 * @Description 账户关联查询
 * @Author fyh
 * Date 2020-6-1518:06
 */
@Repository
@RequiredArgsConstructor
public class AccountDao {

    private  final EntityManager entityManager;

    public List<AccountFindAllByRoleIdDomain> findAllByRoleId(Long roleId, String keyWord, Integer pageNumber, Integer pageSize){
        List<AccountFindAllByRoleIdDomain> accountFindAllByRoleIdDomainList = new ArrayList<>();
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
        Field[] declaredFields = AccountFindAllByRoleIdDomain.class.getDeclaredFields();
        objects.forEach(object -> {
            AccountFindAllByRoleIdDomain accountFindAllByRoleIdDomain = new AccountFindAllByRoleIdDomain();
            try {
                declaredFields[0].setAccessible(true);
                declaredFields[0].set(accountFindAllByRoleIdDomain,Long.valueOf(object[0].toString()));
                declaredFields[1].setAccessible(true);
                declaredFields[1].set(accountFindAllByRoleIdDomain,object[1].toString());
                declaredFields[2].setAccessible(true);
                declaredFields[2].set(accountFindAllByRoleIdDomain,object[2].toString());
                declaredFields[3].setAccessible(true);
                declaredFields[3].set(accountFindAllByRoleIdDomain,Long.valueOf(object[3].toString())==1?true:false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
                accountFindAllByRoleIdDomainList.add(accountFindAllByRoleIdDomain);
            });
        return accountFindAllByRoleIdDomainList;
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
