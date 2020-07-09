package cn.cnyaoshun.form.designer.service;

import cn.cnyaoshun.form.common.PageDataDomain;

import java.util.Map;

public interface FormService {

    PageDataDomain<Map<String,Object>> findByPage(Integer pageNumber,Integer pageSize,Long designerId);

    void delete(String id,Long designerId);

    void save(Map<String,Object> map);

    Map<String,Object> findById(String id,Long designerId);

}
