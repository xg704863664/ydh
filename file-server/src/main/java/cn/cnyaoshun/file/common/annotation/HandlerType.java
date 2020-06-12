package cn.cnyaoshun.file.common.annotation;

import cn.cnyaoshun.file.common.DownloadType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface HandlerType {
    DownloadType value();
}
