package cn.cnyaoshun.oauth.service.listener;

import cn.cnyaoshun.oauth.common.ApiCode;
import cn.cnyaoshun.oauth.common.exception.ExceptionValidation;
import cn.cnyaoshun.oauth.domain.AccountImportDomain;
import cn.cnyaoshun.oauth.service.DealExcelService;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class AccountImportListener extends AnalysisEventListener<AccountImportDomain> {

    private List<AccountImportDomain> accountImportDomainList = new ArrayList<>();

    private final DealExcelService<AccountImportDomain> dealExcelService;

    private static final int BATCH_COUNT = 200;

    @Override
    public void invoke(AccountImportDomain accountImportDomain, AnalysisContext analysisContext) {
        if (StringUtils.isEmpty(accountImportDomain.getAccountName())){
            throw new ExceptionValidation(ApiCode.PARAMETER_ERROR.getCode(),"帐号名不能为空");
        }
        if (StringUtils.isEmpty(accountImportDomain.getPassWord())){
            throw new ExceptionValidation(ApiCode.PARAMETER_ERROR.getCode(),"密码不能为空");
        }
        if (StringUtils.isEmpty(accountImportDomain.getUserName())){
            throw new ExceptionValidation(ApiCode.PARAMETER_ERROR.getCode(),"用户名不能为空");
        }
        if (StringUtils.isEmpty(accountImportDomain.getPhone())){
            throw new ExceptionValidation(ApiCode.PARAMETER_ERROR.getCode(),"手机号不能为空");
        }
        log.info("解析到一条数据:{}", JSON.toJSONString(accountImportDomain));
        accountImportDomainList.add(accountImportDomain);
        if (accountImportDomainList.size() >= BATCH_COUNT) {
            log.info("{}条数据，开始存储数据库.", accountImportDomainList.size());
            dealExcelService.dealData(accountImportDomainList);
            accountImportDomainList.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("{}条数据，开始存储数据库.", accountImportDomainList.size());
        dealExcelService.dealData(accountImportDomainList);
        log.info("所有数据解析完成.");
    }
}
