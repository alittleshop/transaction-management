package com.entity;

/**
 * 业务错误枚举
 * author:kdl
 */
public enum ErrorCodeEnum {
    SYSTEM_ERROR(1000, "系统异常"),
    VALID_ERROR(1001, "参数异常"),
    BIZ_ERROR(1002, "业务异常");

    private int code;
    private String msg;

    ErrorCodeEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
