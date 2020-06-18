package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.dao.*;
import cn.cnyaoshun.oauth.domain.AccountDomainV2;
import cn.cnyaoshun.oauth.domain.AccountDomainV3;
import cn.cnyaoshun.oauth.domain.AccountDomainV4;
import cn.cnyaoshun.oauth.domain.AccountDomainV5;
import cn.cnyaoshun.oauth.entity.Account;
import cn.cnyaoshun.oauth.entity.AccountRole;
import cn.cnyaoshun.oauth.entity.Role;
import cn.cnyaoshun.oauth.entity.User;
import cn.cnyaoshun.oauth.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @ClassName AccountServiceImpl
 * @Description DOTO
 * @Author fyh
 * Date 2020-6-1516:56
 */
@Service
@AllArgsConstructor
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
    public PageDataDomain<AccountDomainV2> findAllByRoleId(Long roleId, String keyWord, Integer pageNumber, Integer pageSize) {
        PageDataDomain<AccountDomainV2> pageDataDomain = new PageDataDomain<>();
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        roleOptional.ifPresent(role -> {
            Integer startPage = (pageNumber-1)*pageSize;
            List<AccountDomainV2> accountDomainV2List = accountDao.findAllByRoleId(roleId, keyWord, startPage, pageSize);
            Long countAccountByRoleId = accountDao.countAccountByRoleId(roleId, keyWord);
            pageDataDomain.setCurrent(pageNumber);
            pageDataDomain.setSize(pageSize);
            Integer total=Integer.parseInt(countAccountByRoleId+"")/pageSize+(Integer.parseInt(countAccountByRoleId+"")%pageSize>0?1:0);
            pageDataDomain.setTotal(countAccountByRoleId);
            pageDataDomain.setPages(total);
            for (AccountDomainV2 accountDomainV2 : accountDomainV2List) {
                accountDomainV2.setRoleName(role.getRoleName());
                pageDataDomain.getRecords().add(accountDomainV2);
            }
        });
        return pageDataDomain;
    }

    /**
     * 分配账户
     * @param accountDomainV5
     * @return
     */
    @Override
    @Transactional
    public Long assignAccount(AccountDomainV5 accountDomainV5) {
        Long roleId = accountDomainV5.getRoleId();
        Set<String> accountNameList = accountDomainV5.getAccountName();
        List<AccountRole> accountRoleList = accountRoleRepository.findAllByRoleId(roleId);
        if(accountRoleList.isEmpty()){
            accountNameList.forEach(accountName -> {
                AccountRole accountRole = new AccountRole();
                Account account = accountRepository.findByAccountName(accountName);
                accountRole.setRoleId(account.getId());
                accountRole.setRoleId(roleId);
                accountRoleRepository.save(accountRole);
            });
        }else{
            //合并去重
            accountRoleList.forEach(accountRole -> {
                Optional<Account> accountOptional = accountRepository.findById(accountRole.getAccountId());
                accountOptional.ifPresent(account -> {
                    accountNameList.add(account.getAccountName());
                });
            });
            accountNameList.forEach(accountName ->{
                Account byAccountName = accountRepository.findByAccountName(accountName);
                List<AccountRole> allByAccountId = accountRoleRepository.findAllByAccountId(byAccountName.getId());
                allByAccountId.forEach(accountRole -> {
                    if(accountRole == null){
                        AccountRole accountRoleNew = new AccountRole();
                        accountRoleNew.setRoleId(accountRole.getRoleId());
                        accountRoleNew.setAccountId(byAccountName.getId());
                        accountRoleRepository.save(accountRoleNew);
                    }
                });
            });
        }
        return roleId;
    }

    @Override
    @Transactional
    public Long update(AccountDomainV4 accountDomainV4) {
        Optional<Account> accountD = accountRepository.findById(accountDomainV4.getId());
        accountD.ifPresent(account -> {
            Account accountU = new Account();
            accountU.setId(account.getId());
            accountU.setState(accountDomainV4.isState());
            accountU.setAccountName(accountDomainV4.getAccountName());
            accountU.setPassword(bCryptPasswordEncoder.encode(accountDomainV4.getPassword())); //密码修改后进行加密处理
            accountU.setUpdateTime(new Date());
            User user = userRepository.findByUserName(accountDomainV4.getUserName());
            accountU.setUserId(user.getId());
            accountRepository.save(accountU);

            List<AccountRole> allByAccountId = accountRoleRepository.findAllByAccountId(accountU.getId());
            allByAccountId.forEach(roleAccount ->{
                accountRoleRepository.deleteAllByAccountId(roleAccount.getAccountId());
            });
            List<Long> roleIdList = accountDomainV4.getRoleIdList();
            roleIdList.forEach(roleId ->{
                AccountRole accountRole = new AccountRole();
                accountRole.setAccountId(accountU.getId());
                accountRole.setRoleId(roleId);
                accountRoleRepository.save(accountRole);
            });
        });
        return accountDomainV4.getId();
    }

    /**
     * 新增
     * @param accountDomainV3
     * @return
     */
    @Override
    @Transactional
    public Long add(AccountDomainV3 accountDomainV3) {

        Account account = new Account();
        account.setAccountName(accountDomainV3.getAccountName());
        account.setPassword(bCryptPasswordEncoder.encode(accountDomainV3.getPassword()));
        account.setUserId(accountDomainV3.getUserId());
        accountRepository.save(account);
        //新建账户角色关联关系
        List<Long> roleIdList = accountDomainV3.getRoleIdList();
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
