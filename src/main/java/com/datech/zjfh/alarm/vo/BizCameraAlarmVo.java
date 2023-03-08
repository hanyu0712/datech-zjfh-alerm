package com.datech.zjfh.alarm.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BizCameraAlarmVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer orgId;
    private Integer cameraId;
    private String orgName;
    private String detail;
    private String area;
    private String cameraName;
    private String cameraCode;
    private String cameraIp;
    private Integer state;
    private Date createTime;
    private Date clearTime;
    private String clearUser;
    private String opinions;
    private String ivsUrl;
    private String ivsAccount;
    private String ivsPassword;
    private String ivsToken;
}
