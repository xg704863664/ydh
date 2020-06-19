package cn.cnyaoshun.oauth.common.annotation;

import cn.cnyaoshun.oauth.common.ExcelDealType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface HandlerType {
    ExcelDealType value();
}
