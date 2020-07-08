package cn.cnyaoshun.form.datasource.service.handler;

import cn.cnyaoshun.form.common.ClassScaner;
import cn.cnyaoshun.form.common.annotation.DynamicType;
import com.google.common.collect.Maps;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class HandlerProcessor implements BeanFactoryPostProcessor {
    private static final String HANDLER_PACKAGE = "cn.cnyaoshun.form.datasource.service";
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        Map<String,Class> handlerMap = Maps.newHashMapWithExpectedSize(1);
        ClassScaner.scan(HANDLER_PACKAGE, DynamicType.class).forEach(clazz ->{
                String type = clazz.getAnnotation(DynamicType.class).value().getType();
                handlerMap.put(type, clazz);
        });
        HandlerContext handlerContext = new HandlerContext(handlerMap);
        configurableListableBeanFactory.registerSingleton(HandlerContext.class.getName(),handlerContext);
    }
}
