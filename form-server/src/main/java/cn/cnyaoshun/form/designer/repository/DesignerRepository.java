package cn.cnyaoshun.form.designer.repository;

import cn.cnyaoshun.form.designer.model.Designer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DesignerRepository extends PagingAndSortingRepository<Designer, Long>, JpaSpecificationExecutor<Designer> {

    List<Designer> findByOrgIdAndStatus(Long orgId, boolean status);
    int countByOrgId(Long orgId);
    List<Designer> findByName(String name);

}
