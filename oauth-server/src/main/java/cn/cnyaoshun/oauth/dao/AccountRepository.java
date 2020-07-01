package cn.cnyaoshun.oauth.dao;

import cn.cnyaoshun.oauth.entity.Account;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by fyh on 2020-6-9.
 */
public interface AccountRepository extends PagingAndSortingRepository<Account, Long>,JpaSpecificationExecutor<Account>{
    Account findByAccountName(String accountName);
    void deleteAllByUserId(Long userId);
    List<Account> findByUserId(Long userId);
    void deleteAllByIdIn(Long id);
}
