package cn.cnyaoshun.oauth.dao;

import cn.cnyaoshun.oauth.entity.Account;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by fyh on 2020-6-9.
 */
public interface AccountRepository extends PagingAndSortingRepository<Account, Long>{
    Account findByAccountName(String accountName);
    void deleteAllByUserId(long userId);
}
