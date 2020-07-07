package cn.cnyaoshun.from.common;

import lombok.Getter;

public enum ApiCode{
    NOT_FOUNT_ACCESS_TOKEN(417,"Token不存在"),
    SUCCESS(0, "请求成功"),
    SYSTEM_BUSY(100, "系统繁忙"),
    REQUEST_TIME_OUT(300, "请求超时"),
    PARAMETER_ERROR(400, "参数错误"),
    NETWORK_ERROR(404, "网络异常"),
    DATA_NOT_EXISTS(600, "数据不存在"),
    ACCESSDENIED_ERROR(501,"无权访问"),
    FAILURE(999, "未知错误");
    @Getter
    private int code;
    @Getter
    private String msg;
    ApiCode(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

}
