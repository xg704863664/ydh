package cn.cnyaoshun.oauth.service;

import cn.cnyaoshun.oauth.common.PageDataDomain;
import cn.cnyaoshun.oauth.domain.ProjectRoleTreeDomain;
import cn.cnyaoshun.oauth.domain.RoleAddDomain;
import cn.cnyaoshun.oauth.domain.RoleUpdateDomain;
import cn.cnyaoshun.oauth.domain.RoleFindAllByProjectIdAndAccountDomain;

import java.util.List;

/**
 * Created by fyh on 2020-6-15.
 */
public interface RoleService {

    Long add(RoleAddDomain roleAddDomain);
    Long delete(Long roleId);
    List<RoleUpdateDomain> findAllByProjectId(Long projectId);
    Long update(RoleUpdateDomain roleUpdateDomain);
    PageDataDomain<RoleFindAllByProjectIdAndAccountDomain> findAll(Integer pageNumber, Integer pageSize,String roleName);
    List<ProjectRoleTreeDomain> findAllRoleTree();
}
