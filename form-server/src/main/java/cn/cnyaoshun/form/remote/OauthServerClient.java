package cn.cnyaoshun.form.remote;

import cn.cnyaoshun.form.common.ReturnJsonData;
import cn.cnyaoshun.form.common.domain.OauthUserListDomain;
import cn.cnyaoshun.form.remote.impl.OauthServerClientFallBack;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "oauth-server", path = "/api/oauth-server",fallback = OauthServerClientFallBack.class)
@Component
public interface OauthServerClient {

//    @HystrixCommand(commandProperties = {
//            @HystrixProperty(name="execution.isolation.thread.interruptOnTimeout", value="TRUE"),
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "7000")
//    },fallbackMethod = "checkToken")
    @RequestMapping("/oauth/check")
    ReturnJsonData<String> checkToken(@RequestHeader(value = "Authorization") String token);

    @RequestMapping("/oauth/getUserInfo")
    ReturnJsonData<OauthUserListDomain> getUserInfo(@RequestHeader(value = "Authorization") String token);
}
