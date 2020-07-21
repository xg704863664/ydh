package cn.cnyaoshun.form.designer.repository;

import cn.cnyaoshun.form.designer.model.Record;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends PagingAndSortingRepository<Record, Long>, JpaSpecificationExecutor<Record> {

    List<Record> findByDataSourceIdAndTableNameOrderByCreateTimeDesc(Long dataSourceId,String tableName);

    List<Record> findByFormId(String formId);

}
