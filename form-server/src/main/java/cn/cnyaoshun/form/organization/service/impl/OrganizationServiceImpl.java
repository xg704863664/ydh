package cn.cnyaoshun.form.organization.service.impl;

import cn.cnyaoshun.form.common.PageDataDomain;
import cn.cnyaoshun.form.designer.model.Designer;
import cn.cnyaoshun.form.organization.model.Organization;
import cn.cnyaoshun.form.organization.repository.OrganizationRepository;
import cn.cnyaoshun.form.organization.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
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
        List<Organization> allOrg = (List<Organization>) organizationRepository.findAll();
        return allOrg;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        organizationRepository.deleteById(id);
    }

    @Override
    public PageDataDomain<Organization> findByPage(Integer pageNum, Integer pageSize, String name) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Specification<Organization> specification = new Specification<Organization>() {
            @Override
            public Predicate toPredicate(Root<Organization> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (name != null) {
                    predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
                }
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        };
        Page<Organization> page = organizationRepository.findAll(specification, pageable);
        PageDataDomain<Organization> result = new PageDataDomain<Organization>();
        result.setCurrent(pageNum);
        result.setSize(pageSize);
        result.setPages(page.getTotalPages());
        result.setTotal(page.getTotalElements());
        result.setRecords(page.getContent());

        return result;
    }


}
