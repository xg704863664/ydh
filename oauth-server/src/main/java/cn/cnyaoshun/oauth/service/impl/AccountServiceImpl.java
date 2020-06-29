package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.dao.*;
import cn.cnyaoshun.oauth.domain.AccountFindAllByRoleIdDomain;
import cn.cnyaoshun.oauth.domain.AccountAddDomain;
import cn.cnyaoshun.oauth.domain.AccountUpdateDomain;
import cn.cnyaoshun.oauth.entity.Account;
import cn.cnyaoshun.oauth.entity.AccountRole;
import cn.cnyaoshun.oauth.entity.Role;
import cn.cnyaoshun.oauth.entity.User;
import cn.cnyaoshun.oauth.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName AccountServiceImpl
 * @Description 账户service实现类
 * @Author fyh
 * Date 2020-6-1516:56
 */
@Service
@AllArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService{

    private final AccountDao accountDao;

    private final AccountRepository accountRepository;

    private final AccountRoleRepository accountRoleRepository;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 根据角色ID去获取账户信息
     * @param roleId
     * @param keyWord
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @Override
    public PageDataDomain<AccountFindAllByRoleIdDomain> findAllByRoleId(Long roleId, String keyWord, Integer pageNumber, Integer pageSize) {
        PageDataDomain<AccountFindAllByRoleIdDomain> pageDataDomain = new PageDataDomain<>();
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        roleOptional.ifPresent(role -> {
            Integer startPage = (pageNumber-1)*pageSize;
            List<AccountFindAllByRoleIdDomain> accountFindAllByRoleIdDomainList = accountDao.findAllByRoleId(roleId, keyWord, startPage, pageSize);
            Long countAccountByRoleId = accountDao.countAccountByRoleId(roleId, keyWord);
            pageDataDomain.setCurrent(pageNumber);
            pageDataDomain.setSize(pageSize);
            Integer total=Integer.parseInt(countAccountByRoleId+"")/pageSize+(Integer.parseInt(countAccountByRoleId+"")%pageSize>0?1:0);
            pageDataDomain.setTotal(countAccountByRoleId);
            pageDataDomain.setPages(total);
            for (AccountFindAllByRoleIdDomain accountFindAllByRoleIdDomain : accountFindAllByRoleIdDomainList) {
                accountFindAllByRoleIdDomain.setRoleName(role.getRoleName());
                pageDataDomain.getRecords().add(accountFindAllByRoleIdDomain);
            }
        });
        log.info("根据角色信息查询账户信息,共有"+pageDataDomain.getTotal()+"条数据");
        return pageDataDomain;
    }

    @Override
    @Transactional
    public Long update(AccountUpdateDomain accountUpdateDomain) {
        Optional<Account> accountD = accountRepository.findById(accountUpdateDomain.getId());
        accountD.ifPresent(account -> {
            Account accountU = new Account();
            accountU.setId(account.getId());
            accountU.setState(accountUpdateDomain.isState());
            accountU.setAccountName(accountUpdateDomain.getAccountName());
            accountU.setPassword(bCryptPasswordEncoder.encode(accountUpdateDomain.getPassword())); //密码修改后进行加密处理
            accountU.setUpdateTime(new Date());
            User user = userRepository.findByUserName(accountUpdateDomain.getUserName());
            accountU.setUserId(user.getId());
            accountRepository.save(accountU);

            List<AccountRole> allByAccountId = accountRoleRepository.findAllByAccountId(accountU.getId());
            allByAccountId.forEach(roleAccount ->{
                accountRoleRepository.deleteAllByAccountId(roleAccount.getAccountId());
            });
            List<Long> roleIdList = accountUpdateDomain.getRoleIdList();
            roleIdList.forEach(roleId ->{
                AccountRole accountRole = new AccountRole();
                accountRole.setAccountId(accountU.getId());
                accountRole.setRoleId(roleId);
                accountRoleRepository.save(accountRole);
            });
        });
        return accountUpdateDomain.getId();
    }

    /**
     * 新增
     * @param accountAddDomain
     * @return
     */
    @Override
    @Transactional
    public Long add(AccountAddDomain accountAddDomain) {

        Account account = new Account();
        account.setAccountName(accountAddDomain.getAccountName());
        account.setPassword(bCryptPasswordEncoder.encode(accountAddDomain.getPassword()));
        account.setUserId(accountAddDomain.getUserId());
        accountRepository.save(account);
        //新建账户角色关联关系
        List<Long> roleIdList = accountAddDomain.getRoleIdList();
        roleIdList.forEach(roleId ->{
            AccountRole accountRole = new AccountRole();
            accountRole.setRoleId(roleId);
            accountRole.setAccountId(account.getId());
            accountRoleRepository.save(accountRole);
        });
        return account.getId();
    }

    @Override
    @Transactional
    public Long delete(Long accountId) {
        accountRepository.deleteById(accountId);
        AccountServiceImpl accountService = (AccountServiceImpl)AopContext.currentProxy();
        accountService.deleteAccountRole(accountId);
        log.info("账户信息删除成功,删除的账号ID为"+accountId);
        return accountId;
    }

    @Async
    @Transactional
    public void deleteAccountRole(Long accountId){
        List<AccountRole> accountRoleList = accountRoleRepository.findAllByAccountId(accountId);
        accountRoleList.forEach(accountRole -> {
            accountRoleRepository.deleteById(accountRole.getId());
        });
    }
}
