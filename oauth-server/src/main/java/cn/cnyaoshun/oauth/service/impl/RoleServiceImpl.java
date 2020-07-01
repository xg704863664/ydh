package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.common.exception.ExceptionValidation;
import cn.cnyaoshun.oauth.dao.ProjectRepository;
import cn.cnyaoshun.oauth.dao.RolePermissionRepository;
import cn.cnyaoshun.oauth.dao.RoleRepository;
import cn.cnyaoshun.oauth.domain.*;
import cn.cnyaoshun.oauth.entity.Department;
import cn.cnyaoshun.oauth.entity.Project;
import cn.cnyaoshun.oauth.entity.Role;
import cn.cnyaoshun.oauth.entity.RolePermission;
import cn.cnyaoshun.oauth.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.dynamic.DynamicType;
import org.apache.el.stream.*;
import org.hibernate.Session;
import org.hibernate.validator.HibernateValidatorFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.management.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.*;
import java.util.Optional;

/**
 * @ClassName RoleServiceImpl
 * @Description 角色service实现类
 * @Author fyh
 * Date 2020-6-1514:40
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final RolePermissionRepository rolePermissionRepository;

    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public Long add(RoleAddDomain roleAddDomain) {
        if(roleAddDomain.getProjectId() == null){
            throw new ExceptionValidation(418,"项目ID不能为空");
        }
        Role role = new Role();
        role.setProjectId(roleAddDomain.getProjectId());
        role.setRoleName(roleAddDomain.getRoleName());
        roleRepository.save(role);
        List<Long> permissionIdList = roleAddDomain.getPermissionIdList();
        permissionIdList.forEach(permissionId ->{
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(role.getId());
            rolePermission.setPermissionId(permissionId);
            rolePermissionRepository.save(rolePermission);
        });
        return role.getId();
    }

    @Override
    @Transactional
    public Long delete(Long roleId) {
        roleRepository.deleteById(roleId);
        RoleServiceImpl roleService = (RoleServiceImpl)AopContext.currentProxy();
        roleService.deleteRolePermissionByRoleId(roleId);
        return roleId;
    }

    @Async
    @Transactional
    public void deleteRolePermissionByRoleId(Long roleId){
        List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(roleId);
        rolePermissions.forEach(rolePermission -> {
            rolePermissionRepository.deleteById(rolePermission.getId());
        });
        log.info("角色信息及其关联信息删除成功");
    }

    @Override
    public List<RoleUpdateDomain> findAllByProjectId(Long projectId) {
        List<Role> roleList = roleRepository.findByProjectId(projectId);
        List<RoleUpdateDomain> roleDomainList = new ArrayList<>();
        roleList.forEach(role -> {
            RoleUpdateDomain roleDomain = new RoleUpdateDomain();
            BeanUtils.copyProperties(role, roleDomain);
            roleDomainList.add(roleDomain);
        });
        log.info("获取角色信息查询成功,共有:"+roleDomainList.size()+"条数据");
        return roleDomainList;
    }

    /**
     * 修改角色信息时 权限信息发生变化
     * @param roleUpdateDomain
     * @return
     */
    @Override
    @Transactional
    public Long update(RoleUpdateDomain roleUpdateDomain) {
        Optional<Role> roleOptional = roleRepository.findById(roleUpdateDomain.getId());
        roleOptional.ifPresent(role -> {
            Role roleR = new Role();
            roleR.setId(role.getId());
            roleR.setRoleName(roleUpdateDomain.getRoleName());
            roleR.setProjectId(roleUpdateDomain.getProjectId());
            roleR.setUpdateTime(roleUpdateDomain.getUpdateTime());

            roleR.setCreateTime(roleUpdateDomain.getCreateTime());
            roleRepository.save(roleR);
            log.info("角色信息修改成功");
            //数据库表中的权限
            List<RolePermission> rolePermissionAll = rolePermissionRepository.findByRoleId(roleR.getId());
            rolePermissionAll.forEach(rolePermission -> {
                rolePermissionRepository.deleteById(rolePermission.getId());
            });
            //修改的权限
            List<Long> permissionIds = roleUpdateDomain.getPermissionIds();
            permissionIds.forEach(permissionId ->{
                RolePermission permission = new RolePermission();
                permission.setPermissionId(permissionId);
                permission.setRoleId(roleR.getId());
                rolePermissionRepository.save(permission);
            });
            log.info("角色关联权限信息修改成功");
        });
        return roleUpdateDomain.getId();
    }

    /**
     * 获取所有得用户角色和ID
     * @return
     */
    @Override
    public PageDataDomain<RoleFindAllByProjectIdAndAccountDomain> findAll(Integer pageNumber, Integer pageSize,String roleName) {
        PageDataDomain<RoleFindAllByProjectIdAndAccountDomain> pageDataDomain = new PageDataDomain();
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        PageRequest page = PageRequest.of(pageNumber - 1, pageSize, sort);
        Specification<Role> specification = new Specification<Role>() {
            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate restrictions = cb.conjunction();
                if(roleName != null && !"".equals(roleName)){
                    Predicate p = cb.like(root.get("roleName"),"%"+roleName+"%");
                    restrictions = cb.and(restrictions,p);
                }
                Predicate pre = cb.and(restrictions);
                return pre;
            }
        };
        Page<Role> roleRepositoryAll = roleRepository.findAll(specification,page);

        roleRepositoryAll.forEach(role -> {
            RoleFindAllByProjectIdAndAccountDomain roleFindAllByProjectIdAndAccountDomain = new RoleFindAllByProjectIdAndAccountDomain();
            roleFindAllByProjectIdAndAccountDomain.setId(role.getId());
            roleFindAllByProjectIdAndAccountDomain.setRoleName(role.getRoleName());
            Optional<Project> projectOptional = projectRepository.findById(role.getProjectId());
            projectOptional.ifPresent(project -> {
                roleFindAllByProjectIdAndAccountDomain.setProjectName(project.getProjectName());
            });
            pageDataDomain.getRecords().add(roleFindAllByProjectIdAndAccountDomain);
        });
        return pageDataDomain;
    }

    @Override
    public List<ProjectRoleTreeDomain> findAllRoleTree() {

        List<Project> projectList = projectRepository.findAll();
        List<ProjectRoleTreeDomain> treeDomainList = new ArrayList<>();
        Map<Long,List<Role>> roleMap = new HashMap<>();
        if(projectList != null){
            projectList.forEach(project -> {
                ProjectRoleTreeDomain projectRoleTreeDomain = new ProjectRoleTreeDomain();
                projectRoleTreeDomain.setProjectId(project.getId());
                projectRoleTreeDomain.setProjectName(project.getProjectName());

            });
        }
        return treeDomainList;
    }

}
