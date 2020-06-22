package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.common.exception.ExceptionValidation;
import cn.cnyaoshun.oauth.dao.PermissionRepository;
import cn.cnyaoshun.oauth.domain.PermissionAddDomain;
import cn.cnyaoshun.oauth.domain.PermissionFindAllByProjectIdDomain;
import cn.cnyaoshun.oauth.domain.PermissionUpdateDomain;
import cn.cnyaoshun.oauth.entity.Permission;
import cn.cnyaoshun.oauth.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class PermissionServiceImpl implements PermissionService{

    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public Long add(PermissionAddDomain permissionAddDomain) {

        boolean existsByPermissionName = permissionRepository.existsByPermissionName(permissionAddDomain.getPermissionName());
        if(existsByPermissionName){
            throw new ExceptionValidation(418,"权限名称已存在,请重新输入");
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
            BeanUtils.copyProperties(permission, permissionFindAllByProjectIdDomain);
            permissionFindAllByProjectIdDomainList.add(permissionFindAllByProjectIdDomain);
        });
        return permissionFindAllByProjectIdDomainList;
    }

    @Override
    @Transactional
    public Long update(PermissionUpdateDomain permissionUpdateDomain) {
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

}
