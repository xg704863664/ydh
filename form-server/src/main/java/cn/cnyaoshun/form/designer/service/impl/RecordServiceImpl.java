package cn.cnyaoshun.form.designer.service.impl;

import cn.cnyaoshun.form.common.exception.ExceptionDataNotExists;
import cn.cnyaoshun.form.designer.model.Record;
import cn.cnyaoshun.form.designer.repository.RecordRepository;
import cn.cnyaoshun.form.designer.service.RecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {

    @Resource
    private RecordRepository recordRepository;

    @Override
    public List<String> findFormIdByDataSourceIdAndTableName(Long dataSourceId, String tableName) {
        List<Record> recordList = recordRepository.findByDataSourceIdAndTableNameOrderByCreateTimeDesc(dataSourceId, tableName);
        List<String> result = new ArrayList<>();
        recordList.forEach(record -> {
            result.add(record.getFormId());
        });
        return result;
    }

    @Override
    @Transactional
    public void save(Record record) {
        recordRepository.save(record);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        recordRepository.deleteById(id);
    }

    @Override
    public Record findByFormId(String formId) {
        List<Record> byFormId = recordRepository.findByFormId(formId);
        return byFormId.size() > 0 ? byFormId.get(0) : null;
    }

    @Override
    public Record findById(Long id) {
        return recordRepository.findById(id).orElseThrow(ExceptionDataNotExists::new);
    }

    @Override
    public void deleteByFormId(String formId) {
        List<Record> recordList = recordRepository.findByFormId(formId);
        if (recordList.size() > 0) {
            recordRepository.deleteAll(recordList);
        }
    }
}
