package cn.cnyaoshun.form.organization.service.impl;

import cn.cnyaoshun.form.organization.model.Organization;
import cn.cnyaoshun.form.organization.repository.OrganizationRepository;
import cn.cnyaoshun.form.organization.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author:
 * @Date: 2020/7/8 17:46
 */
@Service
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {

    @Resource
    private OrganizationRepository organizationRepository;

    @Override
    @Transactional
    public Organization save(Organization organization) {
        return organizationRepository.save(organization);
    }

    @Override
    public List<Organization> getAll() {
        List<Organization> allOrg = organizationRepository.findAll();
        return allOrg;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        organizationRepository.deleteById(id);
    }


}
