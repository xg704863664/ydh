package cn.cnyaoshun.oauth.dao;

import cn.cnyaoshun.oauth.entity.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by fyh on 2020-6-15.
 */
public interface AccountRoleRepository extends JpaRepository<AccountRole, Long> {
    void deleteAllByAccountId(Long accountId);
    List<AccountRole> findAllByAccountId(Long accountId);
    List<AccountRole> findAllByRoleId(Long roleId);
}
