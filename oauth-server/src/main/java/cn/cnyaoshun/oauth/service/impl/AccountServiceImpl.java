package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.common.exception.ExceptionValidation;
import cn.cnyaoshun.oauth.dao.*;
import cn.cnyaoshun.oauth.domain.AccountDomainV2;
import cn.cnyaoshun.oauth.domain.AccountDomainV3;
import cn.cnyaoshun.oauth.domain.AccountDomainV4;
import cn.cnyaoshun.oauth.entity.Account;
import cn.cnyaoshun.oauth.entity.AccountRole;
import cn.cnyaoshun.oauth.entity.Role;
import cn.cnyaoshun.oauth.entity.User;
import cn.cnyaoshun.oauth.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @Override
    @Transactional
    public Long update(AccountDomainV4 accountDomainV4) {
        Optional<Account> accountD = accountRepository.findById(accountDomainV4.getId());
        accountD.ifPresent(account -> {
            Account accountU = new Account();
            accountU.setId(account.getId());
            accountU.setState(account.isState());
            accountU.setAccountName(account.getAccountName());
            accountU.setPassword(account.getPassword());
            accountU.setAvatar(account.getAvatar());
            accountU.setUpdateTime(new Date());
            User user = userRepository.findByUserName(accountDomainV4.getUserName());
            accountU.setUserId(user.getId());
            accountRepository.save(accountU);
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
        if(accountDomainV3.getRoleId() == null){
            throw new ExceptionValidation(418,"选择角色不能为空");
        }
        if(accountDomainV3.getUserName() == null){
            throw new ExceptionValidation(418,"请选择关联用户");
        }
        User user = userRepository.findByUserName(accountDomainV3.getUserName());
        if(user == null){
            throw new ExceptionValidation(418,"请选择用户名称");
        }
        Account account = new Account();
        account.setAccountName(accountDomainV3.getAccountName());
        account.setPassword(accountDomainV3.getPassword());
        account.setState(accountDomainV3.isState());
        account.setUserId(user.getId());
        accountRepository.save(account);
        //新建账户角色关联关系
        AccountRole accountRole = new AccountRole();
        accountRole.setRoleId(accountDomainV3.getRoleId());
        accountRole.setAccountId(account.getId());
        accountRoleRepository.save(accountRole);
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
