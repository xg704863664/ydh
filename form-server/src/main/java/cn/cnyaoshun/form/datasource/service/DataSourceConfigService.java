package cn.cnyaoshun.form.datasource.service;

import cn.cnyaoshun.form.common.PageDataDomain;
import cn.cnyaoshun.form.datasource.model.DataSourceConfig;

import java.util.List;

public interface DataSourceConfigService {

    PageDataDomain<DataSourceConfig> findByPage(Integer pageNumber,Integer pageSize);

    DataSourceConfig save(DataSourceConfig dataSourceConfig);

    void delete(Long id);

    DataSourceConfig findById(Long id);

    boolean connectById(Long id);

    boolean connect(DataSourceConfig dataSourceConfig);

    List<String> findTableNameById(Long id);

    List<String> findFeildNameByIdAndTableName(Long id,String tableName);

    List<DataSourceConfig> findAll();

}
