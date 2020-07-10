package cn.cnyaoshun.form.designer.service.impl;

import cn.cnyaoshun.form.common.PageDataDomain;
import cn.cnyaoshun.form.datasource.model.DataSourceConfig;
import cn.cnyaoshun.form.datasource.service.DataSourceConfigService;
import cn.cnyaoshun.form.datasource.service.DynamicDataSourceConfigService;
import cn.cnyaoshun.form.datasource.service.handler.HandlerContext;
import cn.cnyaoshun.form.designer.model.Designer;
import cn.cnyaoshun.form.designer.service.DesignerService;
import cn.cnyaoshun.form.designer.service.FormService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class FormServiceImpl implements FormService {

    @Resource
    private HandlerContext handlerContext;

    @Resource
    private DesignerService designerService;

    @Resource
    private DataSourceConfigService dataSourceConfigService;

    @Override
    public PageDataDomain<Map<String, Object>> findByPage(Integer pageNumber, Integer pageSize, Long designerId) {
        Designer designer = designerService.findById(designerId);
        Long dataSourceId = designer.getDataSourceId();
        String tableName = designer.getTableName();
        DataSourceConfig dataSourceConfig = dataSourceConfigService.findById(dataSourceId);
        DynamicDataSourceConfigService dynamicDataSourceConfigService = handlerContext.getInstance(dataSourceConfig.getType());
        List<String> feildName = designerService.findFeildNameById(designerId);
        return dynamicDataSourceConfigService.findByPage(pageNumber, pageSize, dataSourceConfig, tableName, feildName);
    }

    @Override
    public void delete(String id, Long designerId) {
        Designer designer = designerService.findById(designerId);
        Long dataSourceId = designer.getDataSourceId();
        String tableName = designer.getTableName();
        DataSourceConfig dataSourceConfig = dataSourceConfigService.findById(dataSourceId);
        DynamicDataSourceConfigService dynamicDataSourceConfigService = handlerContext.getInstance(dataSourceConfig.getType());
        dynamicDataSourceConfigService.deleteData(id, dataSourceConfig, tableName);
    }

    @Override
    public void save(Map<String, Object> map) {
        Long designerId = MapUtils.getLong(map, "designerId");
        Designer designer = designerService.findById(designerId);
        Long dataSourceId = designer.getDataSourceId();
        String tableName = designer.getTableName();
        DataSourceConfig dataSourceConfig = dataSourceConfigService.findById(dataSourceId);
        DynamicDataSourceConfigService dynamicDataSourceConfigService = handlerContext.getInstance(dataSourceConfig.getType());
        List<String> feildName = designerService.findFeildNameById(designerId);
        dynamicDataSourceConfigService.saveData(map, dataSourceConfig, tableName, feildName);
    }

    @Override
    public Map<String, Object> findById(String id, Long designerId) {
        Designer designer = designerService.findById(designerId);
        Long dataSourceId = designer.getDataSourceId();
        String tableName = designer.getTableName();
        DataSourceConfig dataSourceConfig = dataSourceConfigService.findById(dataSourceId);
        DynamicDataSourceConfigService dynamicDataSourceConfigService = handlerContext.getInstance(dataSourceConfig.getType());
        List<String> feildName = designerService.findFeildNameById(designerId);
        return dynamicDataSourceConfigService.findById(id, dataSourceConfig, tableName, feildName);
    }
}
