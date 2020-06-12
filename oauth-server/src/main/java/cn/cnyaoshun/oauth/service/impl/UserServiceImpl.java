package cn.cnyaoshun.oauth.service.impl;


import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.common.exception.ExceptionValidation;
import cn.cnyaoshun.oauth.dao.AccountRepository;
import cn.cnyaoshun.oauth.dao.UserRepository;
import cn.cnyaoshun.oauth.dao.UserDao;
import cn.cnyaoshun.oauth.dao.UserDepartmentRepository;
import cn.cnyaoshun.oauth.domain.OrganizationDomainV2;
import cn.cnyaoshun.oauth.domain.UserDomain;
import cn.cnyaoshun.oauth.domain.UserDomainV2;
import cn.cnyaoshun.oauth.entity.Account;
import cn.cnyaoshun.oauth.entity.Organization;
import cn.cnyaoshun.oauth.entity.User;
import cn.cnyaoshun.oauth.entity.UserDepartment;
import cn.cnyaoshun.oauth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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


    /**
     * 根据部门id获取用户列表
     * @param pageNumber
     * @param pageSize
     * @param departmentId
     * @param name
     * @return
     */
    @Override
    public PageDataDomain<UserDomainV2> departmentList(Integer pageNumber, Integer pageSize, Long departmentId, String name) {
        Integer startPage = (pageNumber-1)*pageSize;
        List<UserDomainV2> crmMemberEntityPage = userDao.queryUserEntitiesByDepartmentId(startPage,pageSize,departmentId,name);
        Long count = userDao.countUserEntitiesByDepartmentId(departmentId,name);
        PageDataDomain<UserDomainV2> pageDataDomain = new PageDataDomain<>();
        pageDataDomain.setCurrent(pageNumber);
        pageDataDomain.setSize(pageSize);
        Integer total=Integer.parseInt(count+"")/pageSize+(Integer.parseInt(count+"")%pageSize>0?1:0);
        pageDataDomain.setPages(total);
        pageDataDomain.setTotal(count);
        pageDataDomain.getRecords().addAll(crmMemberEntityPage);
        return pageDataDomain;
    }

    /**
     * 添加用户
     * @param userDomain
     * @return
     */
    @Override
    @Transactional
    public Long insertUser(UserDomain userDomain) {
        boolean userNumberExists = userRepository.existsByUserNumber(userDomain.getUserNumber());
        if (userNumberExists){
            throw new ExceptionValidation(418,"工号已存在");
        }
        User user = new User();
        BeanUtils.copyProperties(userDomain, user);
        user.setState(true);
        userRepository.save(user);
        Account account = new Account();
        account.setUserId(user.getId());
        account.setAccountName(String.valueOf(user.getUserNumber()));
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
    public Long updateUser(UserDomainV2 userDomainV2){
        Optional<User> userOptional = userRepository.findById(userDomainV2.getId());
        userOptional.ifPresent(user -> {
            BeanUtils.copyProperties(userDomainV2,user);
            user.setId(user.getId());
            user.setUpdateTime(new Date());
            userRepository.save(user);
        });
        return userDomainV2.getId();
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
