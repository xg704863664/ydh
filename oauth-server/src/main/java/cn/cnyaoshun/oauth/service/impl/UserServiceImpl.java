package cn.cnyaoshun.oauth.service.impl;


import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.dao.*;
import cn.cnyaoshun.oauth.domain.*;
import cn.cnyaoshun.oauth.entity.Account;
import cn.cnyaoshun.oauth.entity.Department;
import cn.cnyaoshun.oauth.entity.User;
import cn.cnyaoshun.oauth.entity.UserDepartment;
import cn.cnyaoshun.oauth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @ClassName UserServiceImpl
 * @Description 用户service实现类
 * @Author fyh
 * Date 2020-6-414:40
 */
@Service
@RequiredArgsConstructor
@RefreshScope
@Slf4j
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
     * 获取所有用户名称以及其ID
     * @return
     */
    @Override
    public List<UserFindAllDomain> findAllUserName() {
        Iterable<User> users = userRepository.findAll();
        List<UserFindAllDomain> userFindAllDomainList = new ArrayList<>();
        users.forEach(user -> {
            UserFindAllDomain userFindAllDomain = new UserFindAllDomain();
            userFindAllDomain.setId(user.getId());
            userFindAllDomain.setUserName(user.getUserName());
            userFindAllDomainList.add(userFindAllDomain);
        });
        log.info("获取所有用户信息查询完毕,共有:"+userFindAllDomainList.size()+"条数据");
        return userFindAllDomainList;
    }

    /**
     * 用户调整部门
     * @param userUpdateByDepartmentDomain
     * @return
     */
    @Override
    @Transactional
    public boolean reviseDepartment(UserUpdateByDepartmentDomain userUpdateByDepartmentDomain) {
        Set<Long> departmentIds = userUpdateByDepartmentDomain.getDepartmentIds();
        Set<Long> userIds = userUpdateByDepartmentDomain.getUserIds();
        if(userIds != null){
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
            return true;
        }else{
            return false;
        }

    }

    /**
     * 添加用户
     * @param userAddDomain
     * @return
     */
    @Override
    @Transactional
    public Long add(UserAddDomain userAddDomain) {

        User user = new User();
        user.setIdNo(userAddDomain.getIdNo());
        user.setUserName(userAddDomain.getUserName());
        user.setSex(userAddDomain.getSex());
        user.setAge(userAddDomain.getAge());
        user.setEmail(userAddDomain.getEmail());
        user.setIdNo(userAddDomain.getIdNo());
        user.setPhone(userAddDomain.getPhone());
        userRepository.save(user);
        log.info("用户添加成功");
        Account account = new Account();
        account.setUserId(user.getId());
        account.setState(true);
        account.setAccountName(userAddDomain.getPhone());
        account.setPassword(bCryptPasswordEncoder.encode(modifyPassword));
        accountRepository.save(account);
        log.info("用户关联默认账户添加成功");
        if (userAddDomain.getDepartmentId() != null && userAddDomain.getDepartmentId() > 0){
               Long departmentId1 = userAddDomain.getDepartmentId();
            Optional<Department> departmentOptional = departmentRepository.findById(departmentId1);
            departmentOptional.ifPresent(department -> {
                UserDepartment userDepartment = new UserDepartment();
                userDepartment.setUserId(user.getId());
                userDepartment.setDepartmentId(departmentId1);
                userDepartment.setOrganizationId(department.getOrganizationId());
                userDepartmentRepository.save(userDepartment);
                log.info("用户关联账户关联关系添加成功");
            });
        }
        return user.getId();
    }

    /**
     * 修改
     * @param userUpdateDomain
     * @return
     */
    @Override
    @Transactional
    public Long update(UserUpdateDomain userUpdateDomain){
        Optional<User> userOptional = userRepository.findById(userUpdateDomain.getId());
        userOptional.ifPresent(user -> {
            User userR = new User();
            userR.setId(user.getId());
            userR.setUserName(userUpdateDomain.getUserName());
            userR.setPhone(userUpdateDomain.getPhone());
            userR.setIdNo(userUpdateDomain.getIdNo());
            userR.setEmail(userUpdateDomain.getEmail());
            userR.setSex(userUpdateDomain.getSex());
            userR.setUpdateTime(new Date());
            userR.setAge(userUpdateDomain.getAge());
            userRepository.save(userR);
            log.info("用户信息修改成功");
            List<UserDepartment> userDepartmentList = userDepartmentRepository.findAllByUserId(userR.getId());
            userDepartmentList.forEach(userDepartment -> {
                userDepartmentRepository.deleteById(userDepartment.getId());
            });
            Long departmentId = userUpdateDomain.getDepartmentId();
            Optional<Department> departmentOptional = departmentRepository.findById(departmentId);
            departmentOptional.ifPresent(department -> {
                UserDepartment userDepartment = new UserDepartment();
                userDepartment.setDepartmentId(departmentId);
                userDepartment.setUserId(userR.getId());
                userDepartment.setOrganizationId(department.getOrganizationId());
                userDepartmentRepository.save(userDepartment);
                log.info("用户部门关联信息表修改成功");
            });
        });
        return userUpdateDomain.getId();
    }

    /**
     * 根据部门id获取用户列表
     * @param pageNumber
     * @param pageSize
     * @param departmentId
     * @return
     */
    @Override
    public PageDataDomain<UserFindAllByDepartmentIdDomain> findAll(Long departmentId, String keyWord, Integer pageNumber, Integer pageSize) {
        Optional<Department> departmentOptional = departmentRepository.findById(departmentId);
        PageDataDomain<UserFindAllByDepartmentIdDomain> pageDataDomain = new PageDataDomain<>();
        departmentOptional.ifPresent(department -> {
            Integer startPage = (pageNumber-1)*pageSize;
            List<UserFindAllByDepartmentIdDomain> crmMemberEntityPage = userDao.findUserByDepartmentId(departmentId, keyWord, startPage,pageSize);
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
        log.info("根据部门ID获取用户信息查询完毕,共有:"+pageDataDomain.getTotal()+"条数据");
        return pageDataDomain;
    }

    @Override
    @Transactional
    public Long delete(Long userId) {
        userRepository.deleteById(userId);
        UserServiceImpl userService = (UserServiceImpl)AopContext.currentProxy();
        userService.deleteAccount(userId);
        log.info("用户及其关联关系删除成功");
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
