package cn.cnyaoshun.form.common.annotation;

import cn.cnyaoshun.form.common.DatabaseDriverType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface DynamicType {
    DatabaseDriverType value();
}
