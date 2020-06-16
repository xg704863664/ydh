package cn.cnyaoshun.oauth.service.impl;


import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.common.exception.ExceptionValidation;
import cn.cnyaoshun.oauth.dao.*;
import cn.cnyaoshun.oauth.domain.UserDoaminV3;
import cn.cnyaoshun.oauth.domain.UserDomain;
import cn.cnyaoshun.oauth.domain.UserDomainV2;
import cn.cnyaoshun.oauth.domain.UserDomainV4;
import cn.cnyaoshun.oauth.entity.Account;
import cn.cnyaoshun.oauth.entity.Department;
import cn.cnyaoshun.oauth.entity.User;
import cn.cnyaoshun.oauth.entity.UserDepartment;
import cn.cnyaoshun.oauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by fyh on 2020-6-4.
 */
@Service
@RequiredArgsConstructor
@RefreshScope
public class UserServiceImpl implements UserService {

    @Value("${modify.password}")
    private String modifyPassword;
    private final UserDao userDao;

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserDepartmentRepository userDepartmentRepository;

    private final AccountRoleRepository accountRoleRepository;
    private final DepartmentRepository departmentRepository;


    /**
     * 根据部门ID统计部门下的用户数量
     * @param departmentId
     * @return
     */
    @Override
    public Long countByUserId(Long departmentId) {
        Long countUser = userDepartmentRepository.countByUserId(departmentId);
        return countUser;
    }

    @Override
    public List<UserDoaminV3> findAllUserName() {
        Iterable<User> users = userRepository.findAll();
        List<UserDoaminV3> userDoaminV3List = new ArrayList<>();
        users.forEach(user -> {
            UserDoaminV3 userDoaminV3 = new UserDoaminV3();
            userDoaminV3.setUserName(user.getUserName());
            userDoaminV3List.add(userDoaminV3);
        });
        return userDoaminV3List;
    }

    @Override
    @Transactional
    public boolean reviseDepartment(UserDomainV4 userDomainV4) {
        Set<Long> departmentIds = userDomainV4.getDepartmentIds();
        Set<Long> userIds = userDomainV4.getUserIds();
        if(userIds !=null){
            userIds.forEach(userId->{
                userDepartmentRepository.deleteByUserId(userId);
                if(departmentIds != null){
                    departmentIds.forEach(departmentId ->{
                        UserDepartment userDepartment = new UserDepartment();
                        userDepartment.setDepartmentId(departmentId);
                        userDepartment.setUserId(userId);
                        userDepartmentRepository.save(userDepartment);
                    });
                }
            });
        }
        return false;
    }

    /**
     * 添加用户
     * @param userDomain
     * @return
     */
    @Override
    @Transactional
    public Long add(UserDomain userDomain) {
        boolean userNumberExists = userRepository.existsByUserNo(userDomain.getUserNo());
        if (userNumberExists){
            throw new ExceptionValidation(418,"工号已存在");
        }
        User user = new User();
        BeanUtils.copyProperties(userDomain, user);
        user.setState(true);
        userRepository.save(user);
        Account account = new Account();
        account.setUserId(user.getId());
        account.setAccountName(String.valueOf(user.getUserNo()));
        account.setState(true);
        account.setPassword(bCryptPasswordEncoder.encode(modifyPassword));
        accountRepository.save(account);
        if (userDomain.getDepartmentId() != null && userDomain.getDepartmentId() > 0){
            UserDepartment userDepartment = new UserDepartment();
            userDepartment.setDepartmentId(userDomain.getDepartmentId());
            userDepartment.setOrganizationId(userDomain.getOrganizationId());
            userDepartment.setUserId(user.getId());
            userDepartmentRepository.save(userDepartment);
        }
        return user.getId();
    }

    /**
     * 修改
     * @param userDomainV2
     * @return
     */
    @Override
    @Transactional
    public Long update(UserDomainV2 userDomainV2){
        Optional<User> userOptional = userRepository.findById(userDomainV2.getId());
        userOptional.ifPresent(user -> {
            BeanUtils.copyProperties(userDomainV2,user);
            user.setId(user.getId());
            user.setUpdateTime(new Date());
            userRepository.save(user);
        });
        return userDomainV2.getId();
    }

    /**
     * 根据部门id获取用户列表
     * @param pageNumber
     * @param pageSize
     * @param departmentId
     * @return
     */
    @Override
    public PageDataDomain<UserDomainV2> findAll(Long departmentId, String keyWord, Integer pageNumber, Integer pageSize) {
        Optional<Department> departmentOptional = departmentRepository.findById(departmentId);
        PageDataDomain<UserDomainV2> pageDataDomain = new PageDataDomain<>();
        departmentOptional.ifPresent(department -> {
            Integer startPage = (pageNumber-1)*pageSize;
            List<UserDomainV2> crmMemberEntityPage = userDao.findUserByDepartmentId(departmentId, keyWord, startPage,pageSize);
            crmMemberEntityPage.forEach(crm -> {
                crm.setDepartmentName(department.getDepartmentName());
            });
            Long count = userDao.countUserEntitiesByDepartmentId(departmentId,keyWord);
            pageDataDomain.setCurrent(pageNumber);
            pageDataDomain.setSize(pageSize);
            Integer total=Integer.parseInt(count+"")/pageSize+(Integer.parseInt(count+"")%pageSize>0?1:0);
            pageDataDomain.setPages(total);
            pageDataDomain.setTotal(count);

            pageDataDomain.getRecords().addAll(crmMemberEntityPage);
        });

        return pageDataDomain;
    }

    @Override
    @Transactional
    public Long delete(Long userId) {
        userRepository.deleteById(userId);
        UserServiceImpl userService = (UserServiceImpl)AopContext.currentProxy();
        userService.deleteAccount(userId);
        return userId;
    }

    @Async
    @Transactional
    public void deleteAccount(Long userId){
        accountRepository.deleteAllByUserId(userId);
        //将账户与角色关联表中的数据也删除
        List<Account> accountList = accountRepository.findByUserId(userId);
        accountList.forEach(account -> {
            accountRoleRepository.deleteAllByAccountId(account.getId());
        });
    }
}
