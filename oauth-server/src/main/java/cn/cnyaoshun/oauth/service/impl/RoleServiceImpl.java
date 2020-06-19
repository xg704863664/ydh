package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.common.exception.ExceptionValidation;
import cn.cnyaoshun.oauth.dao.RolePermissionRepository;
import cn.cnyaoshun.oauth.dao.RoleRepository;
import cn.cnyaoshun.oauth.domain.RoleDomain;
import cn.cnyaoshun.oauth.domain.RoleDomainV2;
import cn.cnyaoshun.oauth.domain.RoleDomainV3;
import cn.cnyaoshun.oauth.entity.Role;
import cn.cnyaoshun.oauth.entity.RolePermission;
import cn.cnyaoshun.oauth.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @ClassName RoleServiceImpl
 * @Description DOTO
 * @Author fyh
 * Date 2020-6-1514:40
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final RolePermissionRepository rolePermissionRepository;

    @Override
    @Transactional
    public Long add(RoleDomain roleDomain) {
        if(roleDomain.getProjectId() == null){
            throw new ExceptionValidation(418,"项目ID不能为空");
        }
        Role role = new Role();
        role.setProjectId(roleDomain.getProjectId());
        role.setRoleName(roleDomain.getRoleName());
        roleRepository.save(role);
        List<Long> permissionIdList = roleDomain.getPermissionIdList();
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
    }

    @Override
    public List<RoleDomainV2> findAllByProjectId(Long projectId) {
        List<Role> roleList = roleRepository.findByProjectId(projectId);
        List<RoleDomainV2> roleDomainList = new ArrayList<>();
        roleList.forEach(role -> {
            RoleDomainV2 roleDomain = new RoleDomainV2();
            BeanUtils.copyProperties(role, roleDomain);
            roleDomainList.add(roleDomain);
        });
        return roleDomainList;
    }

    /**
     * 修改角色信息时 权限信息发生变化
     * @param roleDomainV2
     * @return
     */
    @Override
    @Transactional
    public Long update(RoleDomainV2 roleDomainV2) {
        Optional<Role> roleOptional = roleRepository.findById(roleDomainV2.getId());
        roleOptional.ifPresent(role -> {
            Role roleR = new Role();
            roleR.setId(role.getId());
            roleR.setRoleName(roleDomainV2.getRoleName());
            roleR.setProjectId(roleDomainV2.getProjectId());
            roleR.setUpdateTime(new Date());
            roleR.setCreateTime(roleDomainV2.getCreateTime());
            roleRepository.save(role);

            //数据库表中的权限
            List<RolePermission> rolePermissionAll = rolePermissionRepository.findByRoleId(role.getId());
            rolePermissionAll.forEach(rolePermission -> {
                rolePermissionRepository.deleteById(rolePermission.getId());
            });
            //修改的权限
            List<Long> permissionIds = roleDomainV2.getPermissionIds();
            permissionIds.forEach(permissionId ->{
                RolePermission permission = new RolePermission();
                permission.setPermissionId(permissionId);
                permission.setRoleId(role.getId());
                rolePermissionRepository.save(permission);
            });
        });
        return roleDomainV2.getId();
    }

    /**
     * 获取所有得用户角色和ID
     * @return
     */
    @Override
    public List<RoleDomainV3> findAll() {
        List<Role> roleRepositoryAll = roleRepository.findAll();
        List<RoleDomainV3> roleDomainV3List = new ArrayList<>();
        roleRepositoryAll.forEach(role -> {
            RoleDomainV3 roleDomainV3 = new RoleDomainV3();
            roleDomainV3.setId(role.getId());
            roleDomainV3.setRoleName(role.getRoleName());
            roleDomainV3List.add(roleDomainV3);
        });
        return roleDomainV3List;
    }
}
