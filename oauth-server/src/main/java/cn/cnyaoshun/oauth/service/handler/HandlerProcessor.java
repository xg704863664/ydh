package cn.cnyaoshun.oauth.service.handler;

import cn.cnyaoshun.oauth.common.ClassScaner;
import cn.cnyaoshun.oauth.common.annotation.HandlerType;
import com.google.common.collect.Maps;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class HandlerProcessor implements BeanFactoryPostProcessor {
    private static final String HANDLER_PACKAGE = "cn.cnyaoshun.oauth.service";
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        Map<String,Class> handlerMap = Maps.newHashMapWithExpectedSize(1);
        ClassScaner.scan(HANDLER_PACKAGE, HandlerType.class).forEach(clazz ->{
                String type = clazz.getAnnotation(HandlerType.class).value().getType();
                handlerMap.put(type, clazz);
        });
        HandlerContext handlerContext = new HandlerContext(handlerMap);
        configurableListableBeanFactory.registerSingleton(HandlerContext.class.getName(),handlerContext);
    }
}
