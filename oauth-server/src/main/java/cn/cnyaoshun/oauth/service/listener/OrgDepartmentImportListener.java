package cn.cnyaoshun.oauth.service.listener;

import cn.cnyaoshun.oauth.common.ApiCode;
import cn.cnyaoshun.oauth.common.exception.ExceptionValidation;
import cn.cnyaoshun.oauth.domain.OrgDepartmentImportDomain;
import cn.cnyaoshun.oauth.service.DealExcelService;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrgDepartmentImportListener extends AnalysisEventListener<OrgDepartmentImportDomain>{

    private List<OrgDepartmentImportDomain> orgDepartmentImportDomainList = new ArrayList<>();

    private DealExcelService<OrgDepartmentImportDomain> dealExcelService;

    private static final int BATCH_COUNT = 200;

    public OrgDepartmentImportListener(DealExcelService<OrgDepartmentImportDomain> dealExcelService){
          this.dealExcelService = dealExcelService;
    }


    @Override
    public void invoke(OrgDepartmentImportDomain orgDepartmentImportDomain, AnalysisContext analysisContext) {
        if (StringUtils.isEmpty(orgDepartmentImportDomain.getOrgName())){
            throw new ExceptionValidation(ApiCode.PARAMETER_ERROR.getCode(),"组织机构名称不能为空");
        }
        if (StringUtils.isEmpty(orgDepartmentImportDomain.getOneDepartment())){
            throw new ExceptionValidation(ApiCode.PARAMETER_ERROR.getCode(),"一级部门不能为空");
        }
        if (StringUtils.isEmpty(orgDepartmentImportDomain.getUserName())){
            throw new ExceptionValidation(ApiCode.PARAMETER_ERROR.getCode(),"员工名称不能为空");
        }
        if (StringUtils.isEmpty(orgDepartmentImportDomain.getCardNo())){
            throw new ExceptionValidation(ApiCode.PARAMETER_ERROR.getCode(),"员工身份证不能为空");
        }
        if (StringUtils.isEmpty(orgDepartmentImportDomain.getPhone())){
            throw new ExceptionValidation(ApiCode.PARAMETER_ERROR.getCode(),"手机号不能为空");
        }
        log.info("解析到一条数据:{}", JSON.toJSONString(orgDepartmentImportDomain));
        orgDepartmentImportDomainList.add(orgDepartmentImportDomain);
        if (orgDepartmentImportDomainList.size() >= BATCH_COUNT) {
            log.info("{}条数据，开始存储数据库！", orgDepartmentImportDomainList.size());
            dealExcelService.dealData(orgDepartmentImportDomainList);
            log.info("存储数据库成功！");
            // 存储完成清理 list
            orgDepartmentImportDomainList.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("{}条数据，开始存储数据库！", orgDepartmentImportDomainList.size());
        dealExcelService.dealData(orgDepartmentImportDomainList);
        log.info("所有数据解析完成！");

    }
}
