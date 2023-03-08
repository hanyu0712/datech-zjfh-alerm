package com.datech.zjfh.alarm.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginUser implements Serializable {
    private static final long serialVersionUID = 5970869176249386484L;

    /**
     * 登录人id
     */
    private Integer id;

    /**
     * 登录人账号
     */
    private String username;

    /**
     * 登录人名字
     */
    private String realname;
    private String token;


    /**
     * 状态(1：正常 2：冻结 ）
     */
//    private Integer status;


    /**
     * 头像
     */
    private String avatar;

    /**
     * 是否系统组管理员(0：否，1：是)
     */
//    private int groupadmin = 0;

}
