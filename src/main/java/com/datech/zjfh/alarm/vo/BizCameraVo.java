package com.datech.zjfh.alarm.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BizCameraVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String code;
    private String name;
    private String deviceIp;
    private String area;
    private Integer orgId;
    private String orgName;
    private Integer status;
    private Date createTime;
    private Integer alarmType;
}
