package cn.cnyaoshun.form.designer.service;

import cn.cnyaoshun.form.common.PageDataDomain;
import cn.cnyaoshun.form.designer.model.Designer;

import java.util.List;

public interface DesignerService {
    int countByOrgId(Long orgId);

    Designer findById(Long id);

    List<Designer> findByOrgIdAndStatus(Long orgId, boolean status);

    PageDataDomain<Designer> findByPage(Integer pageNum, Integer pageSize, String searchValue);

    Designer save(Designer designer);

    void delete(Long id);

    Designer updateStatus(Long id, boolean status,Long orgId);

    /**
     * 从设计器中取选定的字段名
     *
     * @param id
     * @return
     */
    List<String> findFeildNameById(Long id);

    List<Designer> findByName(String name);

}
