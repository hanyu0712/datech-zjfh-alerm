package com.datech.zjfh.alarm.bean;

import lombok.Data;

@Data
public class TcpRequest {
    // 请求的URI
    private String url;
    //  组织节点 ID
    private Integer orgId;
    private Integer userId;
    // 用户登录获取的token
    private String token;
    // 请求时的时间戳
//    private Long requestTime;
    // 请求头信息，类似http请求的请求头
//    private Map<String,String> header;
    // 请求具体的数据
//    private Object requestBody;
//    原文链接：https://blog.csdn.net/zhaocuit/article/details/121186246
}
