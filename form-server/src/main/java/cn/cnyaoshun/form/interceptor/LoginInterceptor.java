package cn.cnyaoshun.form.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor  {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        String token = request.getHeader("Authorization");
//        if (StringUtils.isEmpty(token)){
//            throw new ExceptionAuth(ApiCode.NOT_FOUNT_ACCESS_TOKEN.getCode(), "token is not null");
//        }
//        OauthServerClient oauthServerClient = AppContextAware.getApplicationContext().getBean(OauthServerClient.class);
//        ReturnJsonData<String> returnJsonData = oauthServerClient.checkToken(token);
//        if (returnJsonData.getCode() == 0) {
//            return true;
//        }
//        throw new ExceptionAuth(returnJsonData.getCode(), returnJsonData.getMsg());
        return true;
    }
}
