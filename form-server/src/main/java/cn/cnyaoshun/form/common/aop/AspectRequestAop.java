package cn.cnyaoshun.form.common.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Component
@Aspect
@Log4j2
public class AspectRequestAop {
    @Pointcut("execution(public * cn.cnyaoshun.form.controller.*.*(..))")
    public void requestRecoder(){
    }

    @Around("requestRecoder()")
    public  Object requestTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object o = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        RequestAttributes requestAttributes ;
        try {
            requestAttributes = RequestContextHolder.currentRequestAttributes();
        }catch (Exception e){
            return o;
        }

        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        if (signature.getDeclaringType().isAnnotationPresent(RestController.class)||signature.getDeclaringType().isAnnotationPresent(Controller.class)) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String ip = request.getHeader("X-Real-IP");
            log.info("X-Real-IP:"+ip);
            log.info("ip: {} url: {} class_method:{} args:{} time:{}", request.getRemoteAddr(), request.getRequestURL().toString(), signature.getDeclaringTypeName() + "." + signature.getName(), proceedingJoinPoint.getArgs(),(endTime-startTime)/1000+"s");
        }
        return o;
    }

    @AfterThrowing(pointcut = "requestRecoder()",throwing= "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        log.error("{} {} Params[{}] ,throws: [{}]", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()), error.getMessage());
    }


}
