package cn.cnyaoshun.form.designer.service;

import cn.cnyaoshun.form.designer.model.Record;

import java.util.List;

public interface RecordService {

    List<String> findFormIdByDesignerId(Long designerId);

    void save(Record record);

    void delete(Long id);

    Record findByFormId(String formId);

    Record findById(Long id);

    void deleteByFormId(String formId);

}
