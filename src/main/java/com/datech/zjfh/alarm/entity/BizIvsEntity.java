package com.datech.zjfh.alarm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("biz_ivs")
public class BizIvsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 线路id
     */
    @TableField(value = "line_id")
    private Integer lineId;

    @TableField(value = "name")
    private String name;

    @TableField(value = "ip")
    private String ip;

    /**
     *  账号
     */
    @TableField(value = "account")
    private String account;
    /**
     *  密码
     */
    @TableField(value = "password")
    private String password;

    @TableField(value = "token")
    private String token;

    /**
     *  在线状态，0：不在线，1：在线
     */
    @TableField(value = "on_line")
    private Integer onLine;


}
