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
        Long countUser = userDepartmentRepository.countUserDepartmentsByDepartmentId(departmentId);
        return countUser;
    }

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
        user.setAge(userAddDomain.getAge());
        user.setEmail(userAddDomain.getEmail());
        user.setIdNo(userAddDomain.getIdNo());
        user.setPhone(userAddDomain.getPhone());
        userRepository.save(user);

        Account account = new Account();
        account.setUserId(user.getId());
        account.setState(true);
        account.setPassword(bCryptPasswordEncoder.encode(modifyPassword));
        accountRepository.save(account);

        if (userAddDomain.getDepartmentId() != null && userAddDomain.getDepartmentId() > 0){
               Long departmentId1 = userAddDomain.getDepartmentId();
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
