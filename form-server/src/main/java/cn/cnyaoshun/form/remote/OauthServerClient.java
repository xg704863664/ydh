package cn.cnyaoshun.form.remote;

import cn.cnyaoshun.form.common.ReturnJsonData;
import cn.cnyaoshun.form.common.domain.OauthUserListDomain;
import cn.cnyaoshun.form.remote.impl.OauthServerClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "oauth-server", path = "/api/oauth-server",fallback = OauthServerClientFallBack.class)
@Component
public interface OauthServerClient {

    @RequestMapping("/oauth/check")
    ReturnJsonData<String> checkToken(@RequestHeader(value = "Authorization") String token);

    @RequestMapping("/oauth/getUserInfo")
    ReturnJsonData<OauthUserListDomain> getUserInfo(@RequestHeader(value = "Authorization") String token);
}
