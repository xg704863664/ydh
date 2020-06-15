package cn.cnyaoshun.oauth.controller;

import cn.cnyaoshun.oauth.common.ApiCode;
import cn.cnyaoshun.oauth.common.ReturnJsonData;
import cn.cnyaoshun.oauth.common.exception.ExceptionAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;


@RestController
@RequestMapping("/oauth")
@Api(description = "TOKEN操作API")
@RequiredArgsConstructor
public class OauthController {

    private final TokenStore tokenStore;


    @ApiOperation(value = "校验TOKEN是否有效",httpMethod = "GET",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/check",method = RequestMethod.GET)
    public ReturnJsonData<String> checkToken(@ApiIgnore OAuth2Authentication oAuth2Authentication){
        if (oAuth2Authentication == null){
            throw new ExceptionAuth(ApiCode.NOT_FOUNT_ACCESS_TOKEN.getCode(),ApiCode.NOT_FOUNT_ACCESS_TOKEN.getMsg());
        }
        OAuth2AccessToken accessToken = tokenStore.getAccessToken(oAuth2Authentication);
        if (accessToken == null){
            throw new ExceptionAuth(ApiCode.NOT_FOUNT_ACCESS_TOKEN.getCode(),ApiCode.NOT_FOUNT_ACCESS_TOKEN.getMsg());
        }
        return  ReturnJsonData.build("success");
    }

    @ApiOperation(value = "退出登录清空TOKEN",httpMethod = "DELETE",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @DeleteMapping("/remove")
    public ReturnJsonData<String> removeToken(@ApiIgnore OAuth2Authentication oAuth2Authentication){
        if (oAuth2Authentication == null){
            throw new ExceptionAuth(ApiCode.NOT_FOUNT_ACCESS_TOKEN.getCode(),ApiCode.NOT_FOUNT_ACCESS_TOKEN.getMsg());
        }
        OAuth2AccessToken accessToken = tokenStore.getAccessToken(oAuth2Authentication);
        tokenStore.removeAccessToken(accessToken);
        return  ReturnJsonData.build("success");
    }
}
