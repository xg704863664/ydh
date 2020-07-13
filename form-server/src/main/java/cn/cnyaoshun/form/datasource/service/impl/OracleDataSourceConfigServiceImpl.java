package cn.cnyaoshun.form.datasource.service.impl;

import cn.cnyaoshun.form.common.DatabaseDriverType;
import cn.cnyaoshun.form.common.PageDataDomain;
import cn.cnyaoshun.form.common.annotation.DynamicType;
import cn.cnyaoshun.form.common.util.DynamicDataSourceUtil;
import cn.cnyaoshun.form.common.util.DynamicTemplate;
import cn.cnyaoshun.form.datasource.model.DataSourceConfig;
import cn.cnyaoshun.form.datasource.service.DynamicDataSourceConfigService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@DynamicType(value = DatabaseDriverType.ORACLE)
public class OracleDataSourceConfigServiceImpl implements DynamicDataSourceConfigService {

    private final DynamicDataSourceUtil dynamicDataSourceUtil;

    @Override
    public List<String> findTableName(DataSourceConfig dataSourceConfig) {
        List<String> result = new ArrayList<>();
        String sql = "select table_name from user_tables";
        List<Map<String, Object>> tables = query(sql, dataSourceConfig);
        tables.forEach(table -> {
            table.entrySet().forEach(stringObjectEntry -> {
                result.add(stringObjectEntry.getValue().toString());
            });
        });
        return result;
    }

    @Override
    public List<String> findFeildNameByIdAndTableName(String tableName, DataSourceConfig dataSourceConfig) {
        String sql = "SELECT * FROM (SELECT column_name FROM user_tab_columns where table_name = upper('" + tableName + "')) t WHERE t.column_name NOT IN (" +
                "select col.column_name from user_constraints con,user_cons_columns col where col.table_name=upper('" + tableName + "') and con.constraint_name=col.constraint_name and con.constraint_type = 'P'" +
                ")";
        List<String> result = new ArrayList<>();
        List<Map<String, Object>> tables = query(sql, dataSourceConfig);
        tables.forEach(table -> {
            table.entrySet().forEach(stringObjectEntry -> {
                result.add(stringObjectEntry.getValue().toString());
            });
        });
        return result;
    }

    @Override
    public PageDataDomain<Map<String, Object>> findByPage(Integer pageNumber, Integer pageSize, DataSourceConfig dataSourceConfig, String tableName, List<String> feildName) {
        String feild = "t.id, ";
        for (String name : feildName) {
            feild += "t." + name + ",";
        }
        feild = feild.endsWith(",") ? feild.substring(0, feild.length()-1) : feild;
        String dataSql = "SELECT * FROM (SELECT ROWNUM AS rowno, " + feild + " FROM " + tableName + " t WHERE ROWNUM <=" + pageNumber * pageSize + " ) table_alias WHERE table_alias.rowno >= " + ((pageNumber - 1) * pageSize + 1);
        String countSql = "SELECT COUNT(1) AS count FROM " + tableName + " where 1=1 ";
        List<Map<String, Object>> list = this.query(dataSql, dataSourceConfig);
        Long count = this.queryCount(countSql, dataSourceConfig);
        PageDataDomain<Map<String, Object>> result = new PageDataDomain<>();
        result.setTotal(count);
        int pages = Integer.parseInt(count + "") / pageSize + (Integer.parseInt(count + "") % pageSize > 0 ? 1 : 0);
        result.setPages(pages);
        result.setSize(pageSize);
        result.setRecords(list);
        result.setCurrent(pageNumber);
        return result;
    }

    @Override
    public void deleteData(String id, DataSourceConfig dataSourceConfig, String tableName) {
        String deleteSql = "DELETE FROM " + tableName + " WHERE id = '" + id + "'";
        execute(deleteSql, dataSourceConfig);
    }

    @Override
    public Map<String, Object> findById(String id, DataSourceConfig dataSourceConfig, String tableName, List<String> feildName) {
        String feild = "id,";
        for (String name : feildName) {
            feild += name + ",";
        }
        feild = feild.endsWith(",") ? feild.substring(0, feild.length() - 1) : feild;
        String sql = "select " + feild + " from " + tableName + "where id = '" + id + "'";
        return queryForObject(sql, dataSourceConfig);
    }

    @Override
    public void saveData(Map<String, Object> map, DataSourceConfig dataSourceConfig, String tableName, List<String> feildName) {
        String id = MapUtils.getString(map, "ID");
        String sql = "";
        if (StringUtils.isNotBlank(id)) {
            String feild = "";
            for (String name : feildName) {
                feild += name + " = '" + MapUtils.getString(map, name) + "' ,";
            }
            feild = feild.endsWith(",") ? feild.substring(0, feild.length() - 1) : feild;
            sql = "update " + tableName + " set " + feild + " where id = '" + id + "'";
        } else {
            String feild = "id,";
            String feildValue = "'" + UUID.randomUUID().toString() + "',";
            for (String name : feildName) {
                feild += name + ",";
                feildValue += "'" + MapUtils.getString(map, name) + "',";
            }
            feild = feild.endsWith(",") ? feild.substring(0, feild.length() - 1) : feild;
            feildValue = feildValue.endsWith(",") ? feildValue.substring(0, feildValue.length() - 1) : feildValue;
            sql = "insert into " + tableName + "(" + feild + ") values(" + feildValue + ")";
        }
        execute(sql, dataSourceConfig);
    }


    private List<Map<String, Object>> query(String sql, DataSourceConfig dataSourceConfig) {
        DynamicTemplate dynamicTemplate = dynamicDataSourceUtil.getDynamicTemplate(dataSourceConfig.getType(), dataSourceConfig.getUrl(), dataSourceConfig.getUsername(), dataSourceConfig.getPassword());
        return dynamicTemplate.getJdbcTemplate().queryForList(sql);
    }

    private Long queryCount(String sql, DataSourceConfig dataSourceConfig) {
        DynamicTemplate dynamicTemplate = dynamicDataSourceUtil.getDynamicTemplate(dataSourceConfig.getType(), dataSourceConfig.getUrl(), dataSourceConfig.getUsername(), dataSourceConfig.getPassword());
        return dynamicTemplate.getJdbcTemplate().queryForObject(sql, new Object[0], Long.class);
    }

    private Map<String, Object> queryForObject(String sql, DataSourceConfig dataSourceConfig) {
        DynamicTemplate dynamicTemplate = dynamicDataSourceUtil.getDynamicTemplate(dataSourceConfig.getType(), dataSourceConfig.getUrl(), dataSourceConfig.getUsername(), dataSourceConfig.getPassword());
        return dynamicTemplate.getJdbcTemplate().queryForObject(sql, new Object[0], Map.class);
    }

    private void execute(String sql, DataSourceConfig dataSourceConfig) {
        DynamicTemplate dynamicTemplate = dynamicDataSourceUtil.getDynamicTemplate(dataSourceConfig.getType(), dataSourceConfig.getUrl(), dataSourceConfig.getUsername(), dataSourceConfig.getPassword());
        dynamicTemplate.getTransactionTemplate().execute(transactionStatus -> {
            dynamicTemplate.getJdbcTemplate().execute(sql);
            return Boolean.TRUE;
        });
    }

}
