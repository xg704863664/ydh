package cn.cnyaoshun.from.common.exception;

import lombok.Data;

@Data
public class ExceptionValidation extends RuntimeException{
    private Integer code;
    public ExceptionValidation(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
