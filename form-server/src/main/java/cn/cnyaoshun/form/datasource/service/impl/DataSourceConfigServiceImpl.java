package cn.cnyaoshun.form.datasource.service.impl;

import cn.cnyaoshun.form.common.DatabaseDriverType;
import cn.cnyaoshun.form.common.PageDataDomain;
import cn.cnyaoshun.form.common.exception.ExceptionDataNotExists;
import cn.cnyaoshun.form.datasource.model.DataSourceConfig;
import cn.cnyaoshun.form.datasource.repository.DataSourceConfigRepository;
import cn.cnyaoshun.form.datasource.service.DataSourceConfigService;
import cn.cnyaoshun.form.datasource.service.DynamicDataSourceConfigService;
import cn.cnyaoshun.form.datasource.service.handler.HandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DataSourceConfigServiceImpl implements DataSourceConfigService {

    @Resource
    private HandlerContext handlerContext;

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
    @Transactional
    public DataSourceConfig save(DataSourceConfig dataSourceConfig) {
        return dataSourceConfigRepository.save(dataSourceConfig);
    }

    @Override
    @Transactional
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
        DynamicDataSourceConfigService dynamicDataSourceConfigService = handlerContext.getInstance(dataSourceConfig.getType());
        List<String> result = dynamicDataSourceConfigService.findTableName(dataSourceConfig);
        return result;
    }

    @Override
    public List<String> findFeildNameByIdAndTableName(Long id, String tableName) {
        DataSourceConfig dataSourceConfig = dataSourceConfigRepository.findById(id).orElseThrow(ExceptionDataNotExists::new);
        DynamicDataSourceConfigService dynamicDataSourceConfigService = handlerContext.getInstance(dataSourceConfig.getType());
        List<String> result = dynamicDataSourceConfigService.findFeildNameByIdAndTableName(tableName, dataSourceConfig);
        return result;
    }

    @Override
    public List<DataSourceConfig> findAll() {
        List<DataSourceConfig> result = new ArrayList<>();
        Iterable<DataSourceConfig> all = dataSourceConfigRepository.findAll();
        all.forEach(dataSourceConfig->{
            result.add(dataSourceConfig);
        });
        return result;
    }

    private boolean testConnect(String url, String type, String username, String password) {
        boolean result = false;
        try {
            Class.forName(DatabaseDriverType.getDatabaseDriver(type));
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
