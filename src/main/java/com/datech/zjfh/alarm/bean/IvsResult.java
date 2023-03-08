package com.datech.zjfh.alarm.bean;

import lombok.Data;

@Data
public class IvsResult {
    String msg;
    String msg2;
    Integer code;

    public IvsResult(Integer code, String msg) {
        this.msg = msg;
        this.code = code;
    }
    public IvsResult(Integer code, String msg, String msg2) {
        this.msg2 = msg2;
        this.msg = msg;
        this.code = code;
    }
}
