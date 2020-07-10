package cn.cnyaoshun.oauth.common.handler;

import cn.cnyaoshun.oauth.common.ApiCode;
import cn.cnyaoshun.oauth.common.ReturnJsonData;
import cn.cnyaoshun.oauth.common.exception.ExceptionAuth;
import cn.cnyaoshun.oauth.common.exception.ExceptionValidation;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionHandle {
    /**
     * @param e
     * @return 验证异常
     */
    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(ExceptionValidation.class)
    public ReturnJsonData doRequiredExceptionHandle(ExceptionValidation e) {
        return createReturnJsonData(e.getCode(), e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ExceptionAuth.class)
    public ReturnJsonData doRequiredExceptionHandle(ExceptionAuth e) {
        return createReturnJsonData(e.getCode(), e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public ReturnJsonData doRequiredExceptionHandle(Exception e) {
        return createReturnJsonData(ApiCode.FAILURE.getCode(), e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(ConstraintViolationException.class)
    public ReturnJsonData doRequiredExceptionHandle(ConstraintViolationException e) {
        return createReturnJsonData(ApiCode.PARAMETER_ERROR.getCode(), e.getMessage().substring(e.getMessage().lastIndexOf(":") + 1).trim());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ReturnJsonData doRequiredExceptionHandle(MethodArgumentNotValidException e) {
        return createReturnJsonData(ApiCode.PARAMETER_ERROR.getCode(), e.getBindingResult().getFieldError().getDefaultMessage().trim());
    }

    private ReturnJsonData createReturnJsonData(int code, String msg) {
        ReturnJsonData returnJsonData = new ReturnJsonData();
        returnJsonData.setCode(code);
        returnJsonData.setMsg(msg);
        return returnJsonData;
    }
}
