package cn.cnyaoshun.form.designer.service;

import cn.cnyaoshun.form.designer.model.Record;

import java.util.List;

public interface RecordService {

    List<String> findFormIdByDataSourceIdAndTableName(Long dataSourceId,String tableName);

    void save(Record record);

    void delete(Long id);

    Record findByFormId(String formId);

    Record findById(Long id);

}
