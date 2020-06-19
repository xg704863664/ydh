package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.common.ExcelDealType;
import cn.cnyaoshun.oauth.common.annotation.HandlerType;
import cn.cnyaoshun.oauth.dao.*;
import cn.cnyaoshun.oauth.domain.OrgDepartmentImportDomain;
import cn.cnyaoshun.oauth.entity.*;
import cn.cnyaoshun.oauth.service.DealExcelService;
import cn.cnyaoshun.oauth.service.listener.OrgDepartmentImportListener;
import com.alibaba.excel.EasyExcel;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@HandlerType(ExcelDealType.ORG_DEPARTMENT_DEAL)
@RequiredArgsConstructor
@RefreshScope
public class OrgDepartmentImportServiceImpl implements DealExcelService<OrgDepartmentImportDomain> {

    private final OrganizationRepository organizationRepository;

    private final DepartmentRepository departmentRepository;

    private final UserDepartmentRepository userDepartmentRepository;

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    @Value("${modify.password}")
    private String modifyPassword;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void dealExcel(MultipartFile multipartFile) {
        try {
            DealExcelService dealExcelService = (DealExcelService) AopContext.currentProxy();
            EasyExcel.read(multipartFile.getInputStream(), OrgDepartmentImportDomain.class, new OrgDepartmentImportListener(dealExcelService)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Transactional
    @Override
    public void dealData(List<OrgDepartmentImportDomain> orgDepartmentImportDomainList) {
        orgDepartmentImportDomainList.stream().forEach(orgDepartmentImportDomain -> {

            Organization organization = organizationRepository.findByOrganizationName(orgDepartmentImportDomain.getOrgName());
            if (organization == null) {
                organization = new Organization();
                organization.setOrganizationName(orgDepartmentImportDomain.getOrgName());
                organization = organizationRepository.save(organization);
            }

            Department oneDepartment = saveDepartment(organization.getId(), null, orgDepartmentImportDomain.getOneDepartment());

            Department twoDepartment = null;
            if (!StringUtils.isEmpty(orgDepartmentImportDomain.getTwoDepartment())) {
                twoDepartment = saveDepartment(organization.getId(), oneDepartment.getId(), orgDepartmentImportDomain.getTwoDepartment());
            }

            Department threeDepartment = null;
            if (!StringUtils.isEmpty(orgDepartmentImportDomain.getThreeDepartment()) && twoDepartment != null) {
                threeDepartment = saveDepartment(organization.getId(), twoDepartment.getId(), orgDepartmentImportDomain.getThreeDepartment());
            }

            User user = userRepository.findByPhone(orgDepartmentImportDomain.getPhone());
            if (user == null) {
                user = new User();
                user.setPhone(orgDepartmentImportDomain.getPhone());
                user.setUserName(orgDepartmentImportDomain.getUserName());
                user.setIdNo(orgDepartmentImportDomain.getCardNo());
                user = userRepository.save(user);
                Account account = new Account();
                account.setUserId(user.getId());
                account.setState(true);
                account.setAccountName(user.getPhone());
                account.setPassword(bCryptPasswordEncoder.encode(modifyPassword));
                accountRepository.save(account);
            }
            if (threeDepartment != null) {
                saveUserDepartment(user, organization.getId(), threeDepartment.getId());
                return;
            }
            if (twoDepartment != null) {
                saveUserDepartment(user, organization.getId(), twoDepartment.getId());
                return;
            }
            saveUserDepartment(user, organization.getId(), oneDepartment.getId());
            return;
        });

    }


    public void saveUserDepartment(User user, Long orgId, Long departmentId) {
        UserDepartment userDepartment = userDepartmentRepository.findByDepartmentIdAndUserId(departmentId, user.getId());
        if (userDepartment == null) {
            userDepartment = new UserDepartment();
            userDepartment.setUserId(user.getId());
            userDepartment.setOrganizationId(orgId);
            userDepartment.setDepartmentId(departmentId);
            userDepartmentRepository.save(userDepartment);
        }
    }

    public Department saveDepartment(Long orgId, Long parentId, String departmentName) {
        Department department = departmentRepository.findByOrganizationIdAndDepartmentName(orgId, departmentName);
        if (department == null) {
            department = new Department();
            department.setOrganizationId(orgId);
            department.setDepartmentName(departmentName);
            department.setParentId(parentId);
            department = departmentRepository.save(department);
        }
        return department;
    }

}
