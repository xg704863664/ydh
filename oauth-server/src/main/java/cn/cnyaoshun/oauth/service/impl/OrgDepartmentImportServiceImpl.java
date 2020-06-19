package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.common.ExcelDealType;
import cn.cnyaoshun.oauth.common.annotation.HandlerType;
import cn.cnyaoshun.oauth.dao.DepartmentRepository;
import cn.cnyaoshun.oauth.dao.OrganizationRepository;
import cn.cnyaoshun.oauth.dao.UserDepartmentRepository;
import cn.cnyaoshun.oauth.domain.OrgDepartmentImportDomain;
import cn.cnyaoshun.oauth.entity.Organization;
import cn.cnyaoshun.oauth.service.DealExcelService;
import cn.cnyaoshun.oauth.service.listener.OrgDepartmentImportListener;
import com.alibaba.excel.EasyExcel;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@HandlerType(ExcelDealType.ORG_DEPARTMENT_DEAL)
@RequiredArgsConstructor
public class OrgDepartmentImportServiceImpl implements DealExcelService<OrgDepartmentImportDomain>{

    private final OrganizationRepository organizationRepository;

    private final DepartmentRepository departmentRepository;

    private final UserDepartmentRepository userDepartmentRepository;

    @Override
    public void dealExcel(MultipartFile multipartFile) {
        try {
            DealExcelService dealExcelService = (DealExcelService) AopContext.currentProxy();
            EasyExcel.read(multipartFile.getInputStream(), OrgDepartmentImportDomain.class,new OrgDepartmentImportListener(dealExcelService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Transactional
    @Override
    public void dealData(List<OrgDepartmentImportDomain> orgDepartmentImportDomainList) {
        orgDepartmentImportDomainList.stream().forEach(orgDepartmentImportDomain -> {
            Organization organization = organizationRepository.findByOrganizationName(orgDepartmentImportDomain.getOrgName());
            if (organization == null){
                organization.setOrganizationName(orgDepartmentImportDomain.getOrgName());
                organization = organizationRepository.save(organization);
            }
//            departmentRepository.
        });

    }


}
