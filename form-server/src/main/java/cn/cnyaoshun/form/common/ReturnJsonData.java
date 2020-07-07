package cn.cnyaoshun.form.common;

import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class ReturnJsonData<T> {
    private T data;
    private String msg;
    private Integer code;

    public static ReturnJsonData build(Object o){
        ReturnJsonData returnJsonData = new ReturnJsonData();
        returnJsonData.setCode(ApiCode.SUCCESS.getCode());
        returnJsonData.setMsg(ApiCode.SUCCESS.getMsg());
        returnJsonData.setData(o);
        return returnJsonData;
    }
}
