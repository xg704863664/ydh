package cn.cnyaoshun.form.datasource.service;

import cn.cnyaoshun.form.common.PageDataDomain;
import cn.cnyaoshun.form.datasource.model.DataSourceConfig;

import java.util.List;
import java.util.Map;

public interface DynamicDataSourceConfigService {

    List<String> findTableName(DataSourceConfig dataSourceConfig);

    List<String> findFeildNameByIdAndTableName(String tableName, DataSourceConfig dataSourceConfig);

    PageDataDomain<Map<String, Object>> findByPage(Integer pageNumber, Integer pageSize, DataSourceConfig dataSourceConfig, String tableName,List<String> feildName);

    void deleteData(String id, DataSourceConfig dataSourceConfig, String tableName);

    Map<String, Object> findById(String id,DataSourceConfig dataSourceConfig, String tableName,List<String> feildName);

    void saveData(Map<String,Object> map,DataSourceConfig dataSourceConfig, String tableName, List<String> feildName);

}
