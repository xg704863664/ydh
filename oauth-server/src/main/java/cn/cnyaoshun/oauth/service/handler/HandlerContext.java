package cn.cnyaoshun.oauth.service.handler;


import cn.cnyaoshun.oauth.common.AppContextAware;
import cn.cnyaoshun.oauth.service.DealExcelService;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class HandlerContext {
    private Map<String,Class> handlerMap;

    public DealExcelService getInstance(String dealType){
        Class clazz = handlerMap.get(dealType);
        if (clazz == null){
            throw new IllegalArgumentException("not support handler for type: "+dealType);
        }
        return (DealExcelService) AppContextAware.getApplicationContext().getBean(clazz);
    }

}
