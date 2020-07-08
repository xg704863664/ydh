package cn.cnyaoshun.form.datasource.service.impl;

import cn.cnyaoshun.form.common.DatabaseDriverType;
import cn.cnyaoshun.form.common.PageDataDomain;
import cn.cnyaoshun.form.common.exception.ExceptionDataNotExists;
import cn.cnyaoshun.form.common.exception.ExceptionValidation;
import cn.cnyaoshun.form.common.util.DynamicDataSourceUtil;
import cn.cnyaoshun.form.common.util.DynamicTemplate;
import cn.cnyaoshun.form.datasource.model.DataSourceConfig;
import cn.cnyaoshun.form.datasource.repository.DataSourceConfigRepository;
import cn.cnyaoshun.form.datasource.service.DataSourceConfigService;
import cn.cnyaoshun.form.datasource.service.DynamicDataSourceConfigService;
import cn.cnyaoshun.form.datasource.service.handler.HandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DataSourceConfigServiceImpl implements DataSourceConfigService {

    @Resource
    private HandlerContext handlerContext;

    @Resource
    private DynamicDataSourceUtil dynamicDataSourceUtil;

    @Resource
    private DataSourceConfigRepository dataSourceConfigRepository;

    @Override
    public PageDataDomain<DataSourceConfig> findByPage(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<DataSourceConfig> page = dataSourceConfigRepository.findAll(pageable);
        PageDataDomain<DataSourceConfig> result = new PageDataDomain<DataSourceConfig>();
        result.setCurrent(pageNumber);
        result.setSize(pageSize);
        result.setPages(page.getTotalPages());
        result.setTotal(page.getTotalElements());
        result.setRecords(page.getContent());
        return result;
    }

    @Override
    public DataSourceConfig save(DataSourceConfig dataSourceConfig) {
        return dataSourceConfigRepository.save(dataSourceConfig);
    }

    @Override
    public void delete(Long id) {
        dataSourceConfigRepository.deleteById(id);
    }

    @Override
    public DataSourceConfig findById(Long id) {
        return dataSourceConfigRepository.findById(id).orElse(null);
    }

    @Override
    public boolean connectById(Long id) {
        DataSourceConfig dataSourceConfig = dataSourceConfigRepository.findById(id).orElseThrow(ExceptionDataNotExists::new);
        return testConnect(dataSourceConfig.getUrl(), dataSourceConfig.getType(), dataSourceConfig.getUsername(), dataSourceConfig.getPassword());
    }

    @Override
    public boolean connect(DataSourceConfig dataSourceConfig) {
        return testConnect(dataSourceConfig.getUrl(), dataSourceConfig.getType(), dataSourceConfig.getUsername(), dataSourceConfig.getPassword());
    }

    @Override
    public List<String> findTableNameById(Long id) {
        DataSourceConfig dataSourceConfig = dataSourceConfigRepository.findById(id).orElseThrow(ExceptionDataNotExists::new);
//        DynamicTemplate dynamicTemplate = dynamicDataSourceUtil.getDynamicTemplate(dataSourceConfig.getType(), dataSourceConfig.getUrl(), dataSourceConfig.getUsername(), dataSourceConfig.getPassword());
//        String sql = "";
//        if ("mysql".equals(dataSourceConfig.getType())) {
//            sql = "show tables";
//        } else if ("oracle".equals(dataSourceConfig.getType())) {
//            sql = "select table_name from user_tables";
//        } else {
//            throw new ExceptionValidation(400, "数据源类型不存在！");
//        }
//        List<Map<String, Object>> tables = dynamicTemplate.getJdbcTemplate().queryForList(sql);
//        tables.forEach(table -> {
//            table.entrySet().forEach(stringObjectEntry -> {
//                result.add(stringObjectEntry.getValue().toString());
//            });
//        });
        DynamicDataSourceConfigService dynamicDataSourceConfigService = handlerContext.getInstance(dataSourceConfig.getType());
        List<String> result = dynamicDataSourceConfigService.findTableName(dataSourceConfig);
        return result;
    }

    @Override
    public List<String> findFeildNameByIdAndTableName(Long id, String tableName) {
        DataSourceConfig dataSourceConfig = dataSourceConfigRepository.findById(id).orElseThrow(ExceptionDataNotExists::new);
//        DynamicTemplate dynamicTemplate = dynamicDataSourceUtil.getDynamicTemplate(dataSourceConfig.getType(), dataSourceConfig.getUrl(), dataSourceConfig.getUsername(), dataSourceConfig.getPassword());
//        String sql = "";
//        if ("mysql".equals(dataSourceConfig.getType())) {
//            sql = "select COLUMN_NAME from information_schema.columns where TABLE_NAME='" + tableName + "'";
//        } else if ("oracle".equals(dataSourceConfig.getType())) {
//            sql = "select t.COLUMN_NAME from USER_TAB_COLUMNS t where t.TABLE_NAME = upper('" + tableName + "')";
//        } else {
//            throw new ExceptionValidation(400, "数据源类型不存在！");
//        }
//        List<Map<String, Object>> tables = dynamicTemplate.getJdbcTemplate().queryForList(sql);
//        tables.forEach(table -> {
//            table.entrySet().forEach(stringObjectEntry -> {
//                result.add(stringObjectEntry.getValue().toString());
//            });
//        });
        DynamicDataSourceConfigService dynamicDataSourceConfigService = handlerContext.getInstance(dataSourceConfig.getType());
        List<String> result = dynamicDataSourceConfigService.findFeildNameByIdAndTableName(tableName,dataSourceConfig);
        return result;
    }

    private boolean testConnect(String url, String type, String username, String password) {
        boolean result = false;
        try {
//            for (DatabaseDriverType databaseDriverType : DatabaseDriverType.values()) {
//                if (databaseDriverType.getType().equals(type)) {
                    Class.forName(DatabaseDriverType.getDatabaseDriver(type));
//                }
//            }
        } catch (ClassNotFoundException e) {
            log.error("驱动类加载失败！");
            return false;
        }
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            result = (conn != null);
            conn.close();
        } catch (SQLException e) {
            log.error("数据库连接失败！");
            return false;
        }
        return result;
    }
}
