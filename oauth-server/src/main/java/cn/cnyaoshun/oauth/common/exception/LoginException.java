package cn.cnyaoshun.oauth.common.exception;

import cn.cnyaoshun.oauth.common.exception.serializer.LoginExceptionSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

@JsonSerialize(using = LoginExceptionSerializer.class)
public class LoginException extends OAuth2Exception {
    public LoginException(String msg) {
        super(msg);
    }
}
