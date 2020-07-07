package cn.cnyaoshun.form.common.exception;

import lombok.Data;

@Data
public class ExceptionAuth extends RuntimeException{

    private Integer code;
    public ExceptionAuth(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
