package cn.cnyaoshun.file.service.handler;

import cn.cnyaoshun.file.common.ClassScaner;
import cn.cnyaoshun.file.common.annotation.HandlerType;
import com.google.common.collect.Maps;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class HandlerProcessor implements BeanFactoryPostProcessor {
    private static final String HANDLER_PACKAGE = "cn.cnyaoshun.file.service";
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        Map<String,Class> handlerMap = Maps.newHashMapWithExpectedSize(2);
        ClassScaner.scan(HANDLER_PACKAGE, HandlerType.class).forEach(clazz ->{
                String type = clazz.getAnnotation(HandlerType.class).value().getType();
                handlerMap.put(type, clazz);
        });
        HandlerContext handlerContext = new HandlerContext(handlerMap);
        configurableListableBeanFactory.registerSingleton(HandlerContext.class.getName(),handlerContext);
    }
}
