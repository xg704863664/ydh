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

@RequiredArgsConstructor
@Service
@DynamicType(value =  DatabaseDriverType.ORACLE)
public class OracleDataSourceConfigServiceImpl implements DynamicDataSourceConfigService {

    private final DynamicDataSourceUtil dynamicDataSourceUtil;

    @Override
    public List<String> findTableName(DataSourceConfig dataSourceConfig) {
        String sql = "select table_name from user_tables";
        return query(sql,dataSourceConfig);
    }

    @Override
    public List<String> findFeildNameByIdAndTableName(String tableName, DataSourceConfig dataSourceConfig) {
        String sql = "select t.COLUMN_NAME from USER_TAB_COLUMNS t where t.TABLE_NAME = upper('" + tableName + "')";
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
