package cn.cnyaoshun.oauth.service.impl;

import cn.cnyaoshun.oauth.common.exception.ExceptionValidation;
import cn.cnyaoshun.oauth.dao.RoleRepository;
import cn.cnyaoshun.oauth.domain.RoleDomain;
import cn.cnyaoshun.oauth.domain.RoleDomainV2;
import cn.cnyaoshun.oauth.entity.Role;
import cn.cnyaoshun.oauth.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Date;
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

    @Override
    @Transactional
    public Long add(RoleDomain roleDomain) {
        if(roleDomain.getProjectId() == null){
            throw new ExceptionValidation(418,"项目ID不能为空");
        }
        Role role = new Role();
        BeanUtils.copyProperties(roleDomain , role);
        roleRepository.save(role);
        return role.getId();
    }

    @Override
    @Transactional
    public Long delete(Long roleId) {
        roleRepository.deleteById(roleId);
        return roleId;
    }

    @Override
    @Transactional
    public Long update(RoleDomainV2 roleDomainV2) {
        Optional<Role> roleOptional = roleRepository.findById(roleDomainV2.getId());
        roleOptional.ifPresent(role -> {
            BeanUtils.copyProperties(roleDomainV2, role);
            role.setId(role.getId());
            role.setUpdateTime(new Date());
            roleRepository.save(role);
        });
        return roleDomainV2.getId();
    }
}
