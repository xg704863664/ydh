package cn.cnyaoshun.oauth.common.exception.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import cn.cnyaoshun.oauth.common.exception.LoginException;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class LoginExceptionSerializer extends StdSerializer<LoginException> {
    protected LoginExceptionSerializer() {
        super(LoginException.class);
    }
    @Override
    public void serialize(LoginException loginException, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("code", String.valueOf(loginException.getHttpErrorCode()));
        jsonGenerator.writeStringField("msg", loginException.getMessage());
        jsonGenerator.writeStringField("timestamp", String.valueOf(new Date().getTime()));
        if (loginException.getAdditionalInformation() != null) {
            for (Map.Entry<String, String> entry : loginException.getAdditionalInformation().entrySet()) {
                jsonGenerator.writeStringField(entry.getKey(), entry.getValue());
            }
        }
        jsonGenerator.writeEndObject();
    }
}
