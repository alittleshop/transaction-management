package com.exception;

import com.entity.BaseResponse;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.StringJoiner;

/**
 * 全局异常处理类
 * author:kdl
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 参数校检异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public BaseResponse<?> handle(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        StringJoiner joiner = new StringJoiner(";");

        for (ObjectError error : bindingResult.getAllErrors()) {
            String defaultMessage = error.getDefaultMessage();
            joiner.add(defaultMessage);
        }
        return BaseResponse.validError(joiner.toString());
    }

}
