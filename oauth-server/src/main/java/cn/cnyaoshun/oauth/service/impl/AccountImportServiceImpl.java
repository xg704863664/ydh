package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.common.ExcelDealType;
import cn.cnyaoshun.oauth.common.annotation.HandlerType;
import cn.cnyaoshun.oauth.dao.AccountRepository;
import cn.cnyaoshun.oauth.dao.UserRepository;
import cn.cnyaoshun.oauth.domain.AccountImportDomain;
import cn.cnyaoshun.oauth.entity.Account;
import cn.cnyaoshun.oauth.entity.User;
import cn.cnyaoshun.oauth.service.DealExcelService;
import cn.cnyaoshun.oauth.service.listener.AccountImportListener;
import com.alibaba.excel.EasyExcel;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@HandlerType(value = ExcelDealType.ACCOUNT_DEAL)
@RequiredArgsConstructor
public class AccountImportServiceImpl implements DealExcelService<AccountImportDomain> {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void dealExcel(MultipartFile multipartFile) {
        try {
            DealExcelService dealExcelService = (DealExcelService) AopContext.currentProxy();
            EasyExcel.read(multipartFile.getInputStream(), AccountImportDomain.class, new AccountImportListener(dealExcelService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void dealData(List<AccountImportDomain> list) {
        list.stream().forEach(accountImportDomain -> {
            User user = userRepository.findByPhone(accountImportDomain.getPhone());
            Long userId = Optional.ofNullable(user).orElseGet(() -> saveUser(accountImportDomain)).getId();
            Account account = accountRepository.findByAccountName(accountImportDomain.getAccountName());
            Optional.ofNullable(account).orElseGet(() -> saveAccount(accountImportDomain, userId));
        });
    }

    public User saveUser(AccountImportDomain accountImportDomain) {
        User user = new User();
        user.setUserName(accountImportDomain.getUserName());
        user.setPhone(accountImportDomain.getPhone());
        return userRepository.save(user);
    }

    public Account saveAccount(AccountImportDomain accountImportDomain, Long userId) {
        Account account = new Account();
        account.setAccountName(accountImportDomain.getAccountName());
        account.setPassword(bCryptPasswordEncoder.encode(accountImportDomain.getPassWord()));
        account.setState(true);
        account.setUserId(userId);
        return accountRepository.save(account);
    }
}
