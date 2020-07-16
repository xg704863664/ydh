package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.common.exception.ExceptionValidation;
import cn.cnyaoshun.oauth.dao.PermissionRepository;
import cn.cnyaoshun.oauth.dao.ProjectRepository;
import cn.cnyaoshun.oauth.dao.RolePermissionRepository;
import cn.cnyaoshun.oauth.dao.RoleRepository;
import cn.cnyaoshun.oauth.domain.*;
import cn.cnyaoshun.oauth.entity.Permission;
import cn.cnyaoshun.oauth.entity.Project;
import cn.cnyaoshun.oauth.entity.Role;
import cn.cnyaoshun.oauth.entity.RolePermission;
import cn.cnyaoshun.oauth.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public Long add(RoleAddDomain roleAddDomain) {
        if(roleAddDomain.getProjectId() == null){
            throw new ExceptionValidation(418,"项目ID不能为空");
        }
        boolean projectIdAndAndRoleName = roleRepository.existsByProjectIdAndAndRoleName(roleAddDomain.getProjectId(), roleAddDomain.getRoleName());
        if(projectIdAndAndRoleName){
            throw new ExceptionValidation(418,"角色名称已存在,请重新输入");
        }
        Role role = new Role();
        role.setProjectId(roleAddDomain.getProjectId());
        role.setRoleName(roleAddDomain.getRoleName());
        roleRepository.save(role);
        List<Long> permissionIdList = roleAddDomain.getPermissionIdList();
        if(permissionIdList != null){
            permissionIdList.forEach(permissionId ->{
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(role.getId());
                rolePermission.setPermissionId(permissionId);
                rolePermissionRepository.save(rolePermission);
            });
        }
        log.info("角色信息新增成功"+ role.getId());
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
        boolean projectIdAndAndRoleName = roleRepository.existsByProjectIdAndAndRoleNameAndIdNot(roleUpdateDomain.getProjectId(), roleUpdateDomain.getRoleName(),roleUpdateDomain.getId());
        if(projectIdAndAndRoleName){
            throw new ExceptionValidation(418,"角色名称已存在,请重新输入");
        }
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
    public PageDataDomain<RoleFindAllByProjectIdAndAccountDomain> findAll(Integer pageNumber, Integer pageSize,String keyWord) {
        PageDataDomain<RoleFindAllByProjectIdAndAccountDomain> pageDataDomain = new PageDataDomain();
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        PageRequest page = PageRequest.of(pageNumber - 1, pageSize, sort);
        Specification<Role> specification = new Specification<Role>() {
            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate restrictions = cb.conjunction();
                if(!StringUtils.isEmpty(keyWord)){
                    restrictions = cb.like(root.get("roleName"),"%"+keyWord+"%");
                }
                return cb.and(restrictions);
            }
        };
        Page<Role> roleRepositoryAll = roleRepository.findAll(specification,page);
        //当前页
        pageDataDomain.setCurrent(pageNumber);
        //每页指定元素
        pageDataDomain.setSize(pageSize);
        //总共多少条数据
        pageDataDomain.setTotal(roleRepositoryAll.getTotalElements());
        roleRepositoryAll.forEach(role -> {
            RoleFindAllByProjectIdAndAccountDomain roleFindAllByProjectIdAndAccountDomain = new RoleFindAllByProjectIdAndAccountDomain();
            roleFindAllByProjectIdAndAccountDomain.setId(role.getId());
            roleFindAllByProjectIdAndAccountDomain.setRoleName(role.getRoleName());
            Optional<Project> projectOptional = projectRepository.findById(role.getProjectId());
            projectOptional.ifPresent(project -> {
                roleFindAllByProjectIdAndAccountDomain.setProjectId(project.getId());
                roleFindAllByProjectIdAndAccountDomain.setProjectName(project.getProjectName());
            });
            List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleId(role.getId());
            List<PermissionIDAndNameDomain> permissionList = rolePermissions.stream().map(rolePermission -> {
                PermissionIDAndNameDomain permissionDomain = new PermissionIDAndNameDomain();
                Optional.ofNullable(rolePermission.getPermissionId()).ifPresent(permissionId ->{
                    Optional<Permission> permissionOptional = permissionRepository.findById(permissionId);
                    permissionOptional.ifPresent(permission -> {
                        permissionDomain.setId(permission.getId());
                        permissionDomain.setPermissionName(permission.getPermissionName());
                    });
                });
                return permissionDomain;
            }).filter(permissionDomain ->permissionDomain.getId()!=null).collect(Collectors.toList());
            roleFindAllByProjectIdAndAccountDomain.setPermissionList(permissionList);
            pageDataDomain.getRecords().add(roleFindAllByProjectIdAndAccountDomain);
        });
        log.info("角色信息获取成功.共有:"+pageDataDomain.getTotal()+"条数据");
        return pageDataDomain;
    }

    @Override
    public List<ProjectRoleTreeDomain> findAllRoleTree() {

        List<Project> projectList = projectRepository.findAll();
        List<ProjectRoleTreeDomain> treeDomainList = new ArrayList<>();
        if(projectList != null){
            projectList.forEach(project -> {
                ProjectRoleTreeDomain projectRoleTreeDomain = new ProjectRoleTreeDomain();
                projectRoleTreeDomain.setProjectId(project.getId());
                projectRoleTreeDomain.setProjectName(project.getProjectName());
                List<Role> roleList = roleRepository.findByProjectId(project.getId());
                roleList.forEach(role -> {
                    RoleDomain roleDomain = new RoleDomain();
                    roleDomain.setId(role.getId());
                    roleDomain.setRoleName(role.getRoleName());
                    roleDomain.setProjectId(role.getProjectId());
                    projectRoleTreeDomain.getChildren().add(roleDomain);
                });
                treeDomainList.add(projectRoleTreeDomain);
            });
        }
        return treeDomainList;
    }

}
