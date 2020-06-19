package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.common.ExcelDealType;
import cn.cnyaoshun.oauth.common.annotation.HandlerType;
import cn.cnyaoshun.oauth.service.DealExcelService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@HandlerType(ExcelDealType.ORG_DEPARTMENT_DEAL)
public class OrgDepartmentImportServiceImpl implements DealExcelService {

    @Override
    public void dealExcel(MultipartFile multipartFile) {
        int a=0;

    }
}
