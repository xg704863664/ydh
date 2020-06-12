package cn.cnyaoshun.file.service.handler;


import cn.cnyaoshun.file.common.AppContextAware;
import cn.cnyaoshun.file.service.DownLoadBaseService;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class HandlerContext {
    private Map<String,Class> handlerMap;

    public DownLoadBaseService getInstance(String type){
        Class clazz = handlerMap.get(type);
        if (clazz == null){
            throw new IllegalArgumentException("not hound handler for type: "+type);
        }
        return (DownLoadBaseService) AppContextAware.getApplicationContext().getBean(clazz);
    }

}
