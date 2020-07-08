package cn.cnyaoshun.form.datasource.service.impl;

import cn.cnyaoshun.form.common.DatabaseDriverType;
import cn.cnyaoshun.form.common.annotation.DynamicType;
import cn.cnyaoshun.form.common.util.DynamicDataSourceUtil;
import cn.cnyaoshun.form.common.util.DynamicTemplate;
import cn.cnyaoshun.form.datasource.model.DataSourceConfig;
import cn.cnyaoshun.form.datasource.service.DynamicDataSourceConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@DynamicType(value = DatabaseDriverType.MYSQL)
public class MysqlDataSourceConfigServiceImpl implements DynamicDataSourceConfigService {

    private final DynamicDataSourceUtil dynamicDataSourceUtil;

    @Override
    public List<String> findTableName(DataSourceConfig dataSourceConfig) {
        String sql = "show tables";
        return query(sql,dataSourceConfig);
    }

    @Override
    public List<String> findFeildNameByIdAndTableName(String tableName, DataSourceConfig dataSourceConfig) {
        String  sql = "select COLUMN_NAME from information_schema.columns where TABLE_NAME='" + tableName + "'";
        return query(sql,dataSourceConfig);
    }


    private List<String> query(String sql,DataSourceConfig dataSourceConfig){
        List<String> result = new ArrayList<>();
        DynamicTemplate dynamicTemplate = dynamicDataSourceUtil.getDynamicTemplate(dataSourceConfig.getType(), dataSourceConfig.getUrl(), dataSourceConfig.getUsername(), dataSourceConfig.getPassword());
        List<Map<String, Object>> tables = dynamicTemplate.getJdbcTemplate().queryForList(sql);
        tables.forEach(table -> {
            table.entrySet().forEach(stringObjectEntry -> {
                result.add(stringObjectEntry.getValue().toString());
            });
        });
        return result;
    }
}
