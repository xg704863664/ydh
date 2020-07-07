package cn.cnyaoshun.from.remote;

import cn.cnyaoshun.from.common.ReturnJsonData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "oauth-server", path = "/api/oauth-server")
public interface OauthServerClient {
    @RequestMapping("/oauth/check")
    ReturnJsonData<String> checkToken(@RequestHeader(value = "Authorization") String token);
}
