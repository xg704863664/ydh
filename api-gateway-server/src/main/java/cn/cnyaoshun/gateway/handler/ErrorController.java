package cn.cnyaoshun.gateway.handler;

import cn.cnyaoshun.gateway.common.ApiCode;
import cn.cnyaoshun.gateway.common.ReturnJsonData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController {

    @RequestMapping("/default-error")
    public ReturnJsonData<String> defaultErrorHandle(){
        return ReturnJsonData.build(ApiCode.PARAMETER_ERROR.getCode(),"请求失败稍后重试");
    }

    @RequestMapping("/file-error")
    public ReturnJsonData<String> fileErrorHandle(){
        return ReturnJsonData.build(ApiCode.PARAMETER_ERROR.getCode(),"文件上传失败稍后重试");
    }
}
