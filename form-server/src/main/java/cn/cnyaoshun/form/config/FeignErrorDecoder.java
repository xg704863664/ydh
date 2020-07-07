package cn.cnyaoshun.form.config;

import cn.cnyaoshun.form.common.ApiCode;
import cn.cnyaoshun.form.common.exception.ExceptionAuth;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            String message = Util.toString(response.body().asReader());
            if (response.status() == 401){
                return new ExceptionAuth(ApiCode.NOT_FOUNT_ACCESS_TOKEN.getCode(),ApiCode.NOT_FOUNT_ACCESS_TOKEN.getMsg());
            }
            return new RuntimeException(message);
        } catch (IOException ignored) {
        }
        return decode(methodKey, response);
    }
}
