package cn.cnyaoshun.oauth.service;

import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.domain.AccountDomainV2;
import cn.cnyaoshun.oauth.domain.AccountDomainV3;
import cn.cnyaoshun.oauth.domain.AccountDomainV4;
import cn.cnyaoshun.oauth.domain.AccountDomainV5;

/**
 * Created by fyh on 2020-6-15.
 */
public interface AccountService {

    Long add(AccountDomainV3 accountDomainV3);
    Long delete(Long accountId);
    PageDataDomain<AccountDomainV2> findAllByRoleId(Long roleId, String keyWord, Integer pageNumber, Integer pageSize );
    Long update(AccountDomainV4 accountDomainV4);
    //分配账户
    Long assignAccount(AccountDomainV5 accountDomainV5);
}
