package cn.cnyaoshun.form.common.exception;

import cn.cnyaoshun.form.common.ApiCode;
import lombok.Data;

@Data
public class ExceptionDataNotExists extends RuntimeException{
    private Integer code;
    public ExceptionDataNotExists(){
        super(ApiCode.DATA_NOT_EXISTS.getMsg());
        this.code = ApiCode.DATA_NOT_EXISTS.getCode();
    }
}
