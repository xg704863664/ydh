package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.common.ApiCode;
import cn.cnyaoshun.oauth.common.exception.ExceptionValidation;
import cn.cnyaoshun.oauth.dao.AccountRepository;
import cn.cnyaoshun.oauth.entity.Account;
import cn.cnyaoshun.oauth.entity.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service("UserDetailServiceImpl")
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Account account = accountRepository.findByAccountName(userName);
        if (account == null){
            throw new ExceptionValidation(ApiCode.DATA_NOT_EXISTS.getCode(),"用户不存在");
        }
        return new UserDetailsImpl(account);
    }
}
