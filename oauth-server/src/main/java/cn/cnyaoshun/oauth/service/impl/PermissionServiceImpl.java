package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.common.exception.ExceptionValidation;
import cn.cnyaoshun.oauth.dao.PermissionRepository;
import cn.cnyaoshun.oauth.dao.ProjectRepository;
import cn.cnyaoshun.oauth.domain.PermissionAddDomain;
import cn.cnyaoshun.oauth.domain.PermissionFindAllByProjectIdDomain;
import cn.cnyaoshun.oauth.domain.PermissionFindAllDomain;
import cn.cnyaoshun.oauth.domain.PermissionUpdateDomain;
import cn.cnyaoshun.oauth.entity.Permission;
import cn.cnyaoshun.oauth.entity.Project;
import cn.cnyaoshun.oauth.service.PermissionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName PermissionServiceImpl
 * @Description 权限service实现类
 * @Author fyh
 * Date 2020/6/1710:04
 */
@Service
@AllArgsConstructor
@Slf4j
public class PermissionServiceImpl implements PermissionService{

    private final PermissionRepository permissionRepository;

    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public Long add(PermissionAddDomain permissionAddDomain) {

        boolean existsByPermissionName = permissionRepository.existsByProjectIdAndPermissionName(permissionAddDomain.getProjectId(),permissionAddDomain.getPermissionName());
        if(existsByPermissionName){
            throw new ExceptionValidation(418,"权限名称已存在,请重新输入");
        }
        boolean projectIdAndAndPermissionType = permissionRepository.existsByProjectIdAndPermissionType(permissionAddDomain.getProjectId(), permissionAddDomain.getPermissionType());
        if(projectIdAndAndPermissionType){
            throw new ExceptionValidation(418,"权限类型已存在,请重新输入");
        }
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionAddDomain, permission);
        permissionRepository.save(permission);
        return permission.getId();
    }

    @Override
    @Transactional
    public Long delete(Long permissionId) {
        permissionRepository.deleteById(permissionId);
        return permissionId;
    }

    @Override
    public List<PermissionFindAllByProjectIdDomain> findAllByProjectId(Long projectId) {

        List<PermissionFindAllByProjectIdDomain> permissionFindAllByProjectIdDomainList = new ArrayList<>();
        List<Permission> byProjectId = permissionRepository.findByProjectId(projectId);
        byProjectId.forEach(permission -> {
            PermissionFindAllByProjectIdDomain permissionFindAllByProjectIdDomain = new PermissionFindAllByProjectIdDomain();
            permissionFindAllByProjectIdDomain.setId(permission.getId());
            permissionFindAllByProjectIdDomain.setPermissionName(permission.getPermissionName());
            permissionFindAllByProjectIdDomain.setPermissionType(permission.getPermissionType());
            permissionFindAllByProjectIdDomain.setProjectId(permission.getProjectId());
            permissionFindAllByProjectIdDomainList.add(permissionFindAllByProjectIdDomain);
        });
        log.info("权限信息获取成功.共有:"+permissionFindAllByProjectIdDomainList.size()+"条记录");
        return permissionFindAllByProjectIdDomainList;
    }

    @Override
    public PageDataDomain<PermissionFindAllDomain> findAll(Integer pageNumber, Integer pageSize,String keyWord) {

        PageDataDomain<PermissionFindAllDomain> pageDataDomain = new PageDataDomain();
        PageRequest page = PageRequest.of(pageNumber - 1, pageSize, Sort.by(Sort.Direction.DESC,"id"));
        Specification<Permission> specification = (Specification<Permission>) (root, criteriaQuery, cb) -> {
            Predicate restrictions = cb.conjunction();
            if(!StringUtils.isEmpty(keyWord)){
                restrictions = cb.and(restrictions,cb.like(root.get("permissionName"),"%"+keyWord+"%"));
                restrictions = cb.or(restrictions,cb.like(root.get("permissionType"),"%"+keyWord+"%"));
            }
            Predicate pre = cb.and(restrictions);
            return pre;
        };
        Page<Permission> permissionPage = permissionRepository.findAll(specification,page);
        PageDataDomain<PermissionFindAllDomain> permissionFindAllDomainPageDataDomain = permissionFindAll(pageDataDomain, pageNumber, pageSize, permissionPage);
        log.info("获取权限信息成功,共有:"+pageDataDomain.getTotal() +"条数据");
        return permissionFindAllDomainPageDataDomain;
    }

    @Override
    @Transactional
    public Long update(PermissionUpdateDomain permissionUpdateDomain) {

        boolean existsByPermissionName = permissionRepository.existsByProjectIdAndPermissionNameAndIdNot(permissionUpdateDomain.getProjectId(),permissionUpdateDomain.getPermissionName(),permissionUpdateDomain.getId());
        if(existsByPermissionName){
            throw new ExceptionValidation(418,"权限名称已存在,请重新输入");
        }
        boolean projectIdAndAndPermissionType = permissionRepository.existsByProjectIdAndPermissionTypeAndIdNot(permissionUpdateDomain.getProjectId(), permissionUpdateDomain.getPermissionType(),permissionUpdateDomain.getId());
        if(projectIdAndAndPermissionType){
            throw new ExceptionValidation(418,"权限类型已存在,请重新输入");
        }
        Optional<Permission> permissionOptional = permissionRepository.findById(permissionUpdateDomain.getId());
        permissionOptional.ifPresent(permission -> {
           Permission permission1 = new Permission();
            BeanUtils.copyProperties(permissionUpdateDomain, permission1);
            permission1.setId(permission.getId());
            permission1.setUpdateTime(new Date());
            permissionRepository.save(permission1);
        });
        return permissionUpdateDomain.getId();
    }

    private PageDataDomain<PermissionFindAllDomain> permissionFindAll(PageDataDomain<PermissionFindAllDomain> pageDataDomain,Integer pageNumber,Integer pageSize,Page<Permission> permissionPage){

        pageDataDomain.setCurrent(pageNumber);
        pageDataDomain.setSize(pageSize);
        pageDataDomain.setTotal(permissionPage.getTotalElements());
        permissionPage.getContent().forEach(permission -> {
            Optional<Project> projectOptional = projectRepository.findById(permission.getProjectId());
            projectOptional.ifPresent(project -> {

                PermissionFindAllDomain permissionFindAllDomain = new PermissionFindAllDomain();
                permissionFindAllDomain.setPermissionName(permission.getPermissionName());
                permissionFindAllDomain.setPermissionType(permission.getPermissionType());
                permissionFindAllDomain.setId(permission.getId());
                permissionFindAllDomain.setProjectId(project.getId());
                permissionFindAllDomain.setProjectName(project.getProjectName());
                pageDataDomain.getRecords().add(permissionFindAllDomain);
            });
        });
        return  pageDataDomain;
    }
}
