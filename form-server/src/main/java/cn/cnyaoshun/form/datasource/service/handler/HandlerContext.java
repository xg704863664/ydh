package cn.cnyaoshun.form.datasource.service.handler;


import cn.cnyaoshun.form.common.AppContextAware;
import cn.cnyaoshun.form.datasource.service.DynamicDataSourceConfigService;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class HandlerContext {
    private Map<String,Class> handlerMap;

    public DynamicDataSourceConfigService getInstance(String type){
        Class clazz = handlerMap.get(type);
        if (clazz == null){
            throw new IllegalArgumentException("数据源类型不存在: "+type);
        }
        return (DynamicDataSourceConfigService) AppContextAware.getApplicationContext().getBean(clazz);
    }

}
