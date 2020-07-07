package cn.cnyaoshun.from.config;

import cn.cnyaoshun.from.common.ApiCode;
import cn.cnyaoshun.from.common.exception.ExceptionAuth;
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
