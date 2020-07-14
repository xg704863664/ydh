package cn.cnyaoshun.form.designer.repository;

import cn.cnyaoshun.form.designer.model.Designer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DesignerRepository extends PagingAndSortingRepository<Designer, Long> {

    List<Designer> findByOrgIdAndStatus(Long orgId, boolean status);

    Page<Designer> findByOrgId(Long orgId, Pageable page);

    Page<Designer> findByOrgIdAndNameLike(Long orgId, String name, Pageable page);
}
