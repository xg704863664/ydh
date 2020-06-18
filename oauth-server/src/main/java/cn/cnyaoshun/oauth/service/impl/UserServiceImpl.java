package cn.cnyaoshun.oauth.service.impl;


import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.common.exception.ExceptionValidation;
import cn.cnyaoshun.oauth.dao.*;
import cn.cnyaoshun.oauth.domain.*;
import cn.cnyaoshun.oauth.entity.Account;
import cn.cnyaoshun.oauth.entity.Department;
import cn.cnyaoshun.oauth.entity.User;
import cn.cnyaoshun.oauth.entity.UserDepartment;
import cn.cnyaoshun.oauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
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

    /**
     * 获取所有用户名称以及其ID
     * @return
     */
    @Override
    public List<UserDomainV3> findAllUserName() {
        Iterable<User> users = userRepository.findAll();
        List<UserDomainV3> userDomainV3List = new ArrayList<>();
        users.forEach(user -> {
            UserDomainV3 userDomainV3 = new UserDomainV3();
            userDomainV3.setId(user.getId());
            userDomainV3.setUserName(user.getUserName());
            userDomainV3List.add(userDomainV3);
        });
        return userDomainV3List;
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
                        Optional<Department> departmentOptional = departmentRepository.findById(departmentId);
                        departmentOptional.ifPresent(department -> {
                            UserDepartment userDepartment = new UserDepartment();
                            userDepartment.setDepartmentId(departmentId);
                            userDepartment.setUserId(userId);
                            userDepartment.setOrganizationId(department.getOrganizationId());
                            userDepartmentRepository.save(userDepartment);
                        });
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
        user.setIdNo(userDomain.getIdNo());
        user.setUserName(userDomain.getUserName());
        user.setAddress(userDomain.getAddress());
        user.setAge(userDomain.getAge());
        user.setEmail(userDomain.getEmail());
        user.setIdType(userDomain.getIdType());
        user.setIdNo(userDomain.getIdNo());
        user.setPhone(userDomain.getPhone());
        user.setState(true);
        userRepository.save(user);

        Account account = new Account();
        account.setUserId(user.getId());
        account.setAccountName(String.valueOf(user.getUserNo()));
        account.setState(true);
        account.setPassword(bCryptPasswordEncoder.encode(modifyPassword));
        accountRepository.save(account);

        if (userDomain.getDepartmentId() != null && userDomain.getDepartmentId() > 0){
               Long departmentId1 = userDomain.getDepartmentId();
            Optional<Department> departmentOptional = departmentRepository.findById(departmentId1);
            departmentOptional.ifPresent(department -> {
                UserDepartment userDepartment = new UserDepartment();
                userDepartment.setUserId(user.getId());
                userDepartment.setDepartmentId(departmentId1);
                userDepartment.setOrganizationId(department.getOrganizationId());
                userDepartmentRepository.save(userDepartment);
            });
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
            User userR = new User();
            userR.setId(user.getId());
            userR.setUserNo(userDomainV2.getUserNo());
            userR.setUserName(userDomainV2.getUserName());
            userR.setPhone(userDomainV2.getPhone());
            userR.setIdType(userDomainV2.getIdType());
            userR.setIdNo(userDomainV2.getIdNo());
            userR.setEmail(userDomainV2.getEmail());
            userR.setSex(userDomainV2.getSex());
            userR.setAddress(userDomainV2.getAddress());
            userR.setUpdateTime(new Date());
            userR.setState(userDomainV2.isState());
            userR.setAge(userDomainV2.getAge());
            userRepository.save(userR);

            List<UserDepartment> userDepartmentList = userDepartmentRepository.findAllByUserId(userR.getId());
            userDepartmentList.forEach(userDepartment -> {
                userDepartmentRepository.deleteById(userDepartment.getId());
            });
            Long departmentId = userDomainV2.getDepartmentId();
            Optional<Department> departmentOptional = departmentRepository.findById(departmentId);
            departmentOptional.ifPresent(department -> {
                UserDepartment userDepartment = new UserDepartment();
                userDepartment.setDepartmentId(departmentId);
                userDepartment.setUserId(userR.getId());
                userDepartment.setOrganizationId(department.getOrganizationId());
                userDepartmentRepository.save(userDepartment);
            });

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
    public PageDataDomain<UserDomainV5> findAll(Long departmentId, String keyWord, Integer pageNumber, Integer pageSize) {
        Optional<Department> departmentOptional = departmentRepository.findById(departmentId);
        PageDataDomain<UserDomainV5> pageDataDomain = new PageDataDomain<>();
        departmentOptional.ifPresent(department -> {
            Integer startPage = (pageNumber-1)*pageSize;
            List<UserDomainV5> crmMemberEntityPage = userDao.findUserByDepartmentId(departmentId, keyWord, startPage,pageSize);
            crmMemberEntityPage.forEach(crm -> {
                crm.setDepartmentId(department.getId());
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
        //删除部门用户关联表关系
        List<UserDepartment> allByUserId = userDepartmentRepository.findAllByUserId(userId);
        allByUserId.forEach(userDepartment -> {
            userDepartmentRepository.deleteById(userDepartment.getId());
        });

        accountRepository.deleteAllByUserId(userId);
        //将账户与角色关联表中的数据也删除
        List<Account> accountList = accountRepository.findByUserId(userId);
        accountList.forEach(account -> {
            accountRoleRepository.deleteAllByAccountId(account.getId());
        });
    }
}
