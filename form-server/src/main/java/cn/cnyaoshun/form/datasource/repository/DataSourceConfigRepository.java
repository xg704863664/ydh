package cn.cnyaoshun.form.datasource.repository;

import cn.cnyaoshun.form.datasource.model.DataSourceConfig;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSourceConfigRepository extends PagingAndSortingRepository<DataSourceConfig,Long> {
}
