package com.datech.zjfh.alarm.bean;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 7175778217700408538L;

    private boolean success = true;

    private Integer code = 0;

    private String message = "操作成功";

    private T result;

    private long timestamp = System.currentTimeMillis();

    public Result() {

    }

    public Result<T> success(String message) {
        this.message = message;
        this.code = HttpStatus.OK.value();
        this.success = true;
        return this;
    }

    public static<T> Result<T> OK() {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(HttpStatus.OK.value());
        return r;
    }

    public static<T> Result<T> OK(T data) {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(HttpStatus.OK.value());
        r.setResult(data);
        return r;
    }

    public static<T> Result<T> OK(String msg, T data) {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(HttpStatus.OK.value());
        r.setMessage(msg);
        r.setResult(data);
        return r;
    }

    public static Result<Object> error(String msg) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
    }

    public static Result<Object> error(int code, String msg) {
        Result<Object> r = new Result<Object>();
        r.setCode(code);
        r.setMessage(msg);
        r.setSuccess(false);
        return r;
    }

    public Result<T> error500(String message) {
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.message = message;
        this.success = false;
        return this;
    }

    public static Result<Object> paramError() {
        return error("参数错误");
    }

    public static Result<Object> nonRecord() {
        return error("记录不存在");
    }

    public static Result<Object> fail() {
        return error("操作失败");
    }

}
