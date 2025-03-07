package com.entity;


import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * 自定义响应对象
 * author:kdl
 * @param <T>
 */
@Data
public class BaseResponse<T> {
    private int code;
    private String message;
    private T data;

    public BaseResponse(int code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> BaseResponse<T> successData(T data){
        return new BaseResponse<>(HttpStatus.OK.value(), "success" ,data);
    }

    public static <T> BaseResponse<T> success() {
        return successData(null);
    }

    public static <T> BaseResponse<T> bizError(String message) {
        return new BaseResponse<>(ErrorCodeEnum.BIZ_ERROR.getCode(), message, null);
    }

    public static <T> BaseResponse<T> validError(String message) {
        return new BaseResponse<>(ErrorCodeEnum.VALID_ERROR.getCode(), message, null);
    }

    public static <T> BaseResponse<T> systemError(String message) {
        return new BaseResponse<>(ErrorCodeEnum.SYSTEM_ERROR.getCode(), message, null);
    }
}
