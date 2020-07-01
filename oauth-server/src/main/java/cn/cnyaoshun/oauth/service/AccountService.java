package cn.cnyaoshun.oauth.service;

import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.domain.AccountFindAllByRoleIdDomain;
import cn.cnyaoshun.oauth.domain.AccountAddDomain;
import cn.cnyaoshun.oauth.domain.AccountFindAllDomain;
import cn.cnyaoshun.oauth.domain.AccountUpdateDomain;

/**
 * Created by fyh on 2020-6-15.
 */
public interface AccountService {

    Long add(AccountAddDomain accountAddDomain);
    Long delete(Long accountId);
    PageDataDomain<AccountFindAllByRoleIdDomain> findAllByRoleId(Long roleId, String keyWord, Integer pageNumber, Integer pageSize );
    Long update(AccountUpdateDomain accountUpdateDomain);
    PageDataDomain<AccountFindAllDomain> findAll(Integer pageNumber,Integer pageSize,String keyWord);
    Long reloadPassword(Long accountId);
}
