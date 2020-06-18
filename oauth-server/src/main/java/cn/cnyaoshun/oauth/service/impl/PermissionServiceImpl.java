package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.common.exception.ExceptionValidation;
import cn.cnyaoshun.oauth.dao.PermissionRepository;
import cn.cnyaoshun.oauth.dao.RolePermissionRepository;
import cn.cnyaoshun.oauth.dao.RoleRepository;
import cn.cnyaoshun.oauth.domain.PermissionDomain;
import cn.cnyaoshun.oauth.domain.PermissionDomainV2;
import cn.cnyaoshun.oauth.domain.PermissionDomainV3;
import cn.cnyaoshun.oauth.entity.Permission;
import cn.cnyaoshun.oauth.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @ClassName PermissionServiceImpl
 * @Description DOTO
 * @Author fyh
 * Date 2020/6/1710:04
 */
@Service
@AllArgsConstructor
public class PermissionServiceImpl implements PermissionService{

    private final PermissionRepository permissionRepository;

    private final RoleRepository roleRepository;

    private final RolePermissionRepository rolePermissionRepository;

    @Override
    @Transactional
    public Long add(PermissionDomain permissionDomain) {

        boolean existsByPermissionName = permissionRepository.existsByPermissionName(permissionDomain.getPermissionName());
        if(existsByPermissionName){
            throw new ExceptionValidation(418,"权限名称已存在,请重新输入");
        }
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionDomain, permission);
        permission.setState(true);
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
    public List<PermissionDomainV2> findAllByProjectId(Long projectId) {

        List<PermissionDomainV2> permissionDomainV2List = new ArrayList<>();
        List<Permission> byProjectId = permissionRepository.findByProjectId(projectId);
        byProjectId.forEach(permission -> {
            PermissionDomainV2 permissionDomainV2 = new PermissionDomainV2();
            BeanUtils.copyProperties(permission, permissionDomainV2);
            permissionDomainV2List.add(permissionDomainV2);
        });
        return permissionDomainV2List;
    }

    @Override
    @Transactional
    public Long update(PermissionDomainV3 permissionDomainV3) {
        Optional<Permission> permissionOptional = permissionRepository.findById(permissionDomainV3.getId());
        permissionOptional.ifPresent(permission -> {
           Permission permission1 = new Permission();
            BeanUtils.copyProperties(permissionDomainV3, permission1);
            permission1.setId(permission.getId());
            permission1.setUpdateTime(new Date());
            permissionRepository.save(permission1);
        });
        return permissionDomainV3.getId();
    }

}
