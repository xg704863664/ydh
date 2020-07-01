package cn.cnyaoshun.oauth.controller;

import cn.cnyaoshun.oauth.common.ApiCode;
import cn.cnyaoshun.oauth.common.ReturnJsonData;
import cn.cnyaoshun.oauth.common.exception.ExceptionAuth;
import cn.cnyaoshun.oauth.domain.OauthUserListDomain;
import cn.cnyaoshun.oauth.service.OauthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;


@RestController
@RequestMapping("/oauth")
@Api(description = "TOKEN操作")
@RequiredArgsConstructor
public class OauthController {

    private final TokenStore tokenStore;

    private final OauthService oauthService;


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

    @ApiOperation(value = "根据TOKEN和ProjectId,获取账号信息",httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "/getAllUserInfo/{projectId}",method = RequestMethod.GET)
    public ReturnJsonData<OauthUserListDomain> getAllUserInfo(@ApiIgnore OAuth2Authentication oAuth2Authentication, @NotNull @ApiParam(value = "项目ID",required = true) @PathVariable(name = "projectId") Long projectId){
        OauthUserListDomain allUserInfo = oauthService.getAllUserInfo(oAuth2Authentication, projectId);
        return ReturnJsonData.build(allUserInfo);
    }
}
