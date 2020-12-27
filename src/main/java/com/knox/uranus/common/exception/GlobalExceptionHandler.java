package com.knox.uranus.common.exception;

import com.knox.uranus.common.api.CommonResult;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 全局异常处理
 *
 * @author knox
 * @date 2020/2/27
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 系统接口异常
     *
     * @param e 系统异常
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ApiException.class)
    public CommonResult<Map<String, Object>> handle(ApiException e) {
        if (e.getErrorCode() != null) {
            return CommonResult.failed(e.getErrorCode());
        }
        return CommonResult.failed(e.getMessage());
    }

    /**
     * validate参数校验异常
     *
     * @param e 参数校验异常
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public CommonResult<Map<String, Object>> handleValidException(MethodArgumentNotValidException e) {
        String message = null;
        if (e.getBindingResult().hasErrors()) {
            FieldError fieldError = e.getBindingResult().getFieldError();
            if (fieldError != null) {
                message = fieldError.getField() + fieldError.getDefaultMessage();
            }
        }
        return CommonResult.validateFailed(message);
    }

    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public CommonResult<Map<String, Object>> handleValidException(BindException e) {
        String message = null;
        if (e.getBindingResult().hasErrors()) {
            FieldError fieldError = e.getBindingResult().getFieldError();
            if (fieldError != null) {
                message = fieldError.getField() + fieldError.getDefaultMessage();
            }
        }
        return CommonResult.validateFailed(message);
    }
}
