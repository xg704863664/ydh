package cn.cnyaoshun.form.remote.impl;

import cn.cnyaoshun.form.common.ApiCode;
import cn.cnyaoshun.form.common.ReturnJsonData;
import cn.cnyaoshun.form.remote.OauthServerClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OauthServerClientFallBack implements OauthServerClient {
    @Override
    public ReturnJsonData<String> checkToken(String token) {
        log.info("请求非200或超时进入熔断机制请求token: "+token);
        return ReturnJsonData.build(ApiCode.NOT_FOUNT_ACCESS_TOKEN.getCode(),"token 校验失败");
    }
}
