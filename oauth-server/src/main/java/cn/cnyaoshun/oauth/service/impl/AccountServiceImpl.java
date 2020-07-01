package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.dao.*;
import cn.cnyaoshun.oauth.domain.*;
import cn.cnyaoshun.oauth.entity.Account;
import cn.cnyaoshun.oauth.entity.AccountRole;
import cn.cnyaoshun.oauth.entity.Role;
import cn.cnyaoshun.oauth.entity.User;
import cn.cnyaoshun.oauth.service.AccountService;
import javassist.runtime.Desc;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
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
            accountU.setUserId(accountUpdateDomain.getUserId());
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

    @Override
    public PageDataDomain<AccountFindAllDomain> findAll(Integer pageNumber, Integer pageSize, String keyWord) {

        PageDataDomain<AccountFindAllDomain> pageDataDomain = new PageDataDomain<>();
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        PageRequest page = PageRequest.of(pageNumber-1,pageSize,sort);
        Specification<Account> accountSpecification = (Specification<Account>) (root, criteriaQuery, cb) -> {
            Predicate restrictions = cb.conjunction();
            if(keyWord != null && !"".equals(keyWord)){
                Predicate predicate1 = cb.like(root.get("accountName"),"%"+keyWord+"%");
                restrictions = cb.and(restrictions,predicate1);
            }
            Predicate pre = cb.and(restrictions);
            return pre;
        };
        Page<Account> accountRepositoryAll = accountRepository.findAll(accountSpecification,page);
        pageDataDomain.setCurrent(pageNumber-1);
        pageDataDomain.setPages(pageSize);
        pageDataDomain.setTotal(accountRepositoryAll.getTotalElements());
        accountRepositoryAll.forEach(account -> {
            AccountFindAllDomain accountFindAllDomain = new AccountFindAllDomain();
            accountFindAllDomain.setId(account.getId());
            accountFindAllDomain.setAccountName(account.getAccountName());
            Optional.ofNullable(account.getUserId()).ifPresent(userId -> {
                Optional<User> userOptional = userRepository.findById(userId);
                userOptional.ifPresent(user -> {
                    accountFindAllDomain.setUserId(user.getId());
                    accountFindAllDomain.setUserName(user.getUserName());
                });
            });
            List<AccountRole> accountRoleList = accountRoleRepository.findAllByAccountId(account.getId());
            List<String> roleNameList = new ArrayList<>();
            List<RoleDomain> roleDomainList = new ArrayList<>();
            accountRoleList.forEach(accountRole -> {
                RoleDomain roleDomain = new RoleDomain();
                Optional<Role> roleOptional = roleRepository.findById(accountRole.getRoleId());
                roleOptional.ifPresent(role -> {
                    roleNameList.add(role.getRoleName());
                    roleDomain.setId(role.getId());
                    roleDomain.setRoleName(role.getRoleName());
                    roleDomain.setProjectId(role.getProjectId());
                    roleDomainList.add(roleDomain);
                });
            });
            accountFindAllDomain.setRoleName(roleNameList);
            accountFindAllDomain.setRoleList(roleDomainList);
            pageDataDomain.getRecords().add(accountFindAllDomain);
        });
        return pageDataDomain;
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
        account.setState(true);
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
