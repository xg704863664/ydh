package cn.cnyaoshun.form.datasource.service;

import cn.cnyaoshun.form.datasource.model.DataSourceConfig;

import java.util.List;

public interface DynamicDataSourceConfigService {

    List<String> findTableName(DataSourceConfig dataSourceConfig);

    List<String> findFeildNameByIdAndTableName(String tableName, DataSourceConfig dataSourceConfig);
}
