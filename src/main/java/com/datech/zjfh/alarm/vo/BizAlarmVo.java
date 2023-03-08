package com.datech.zjfh.alarm.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BizAlarmVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String lineName;
    private String alarmName;
    private String imageId;
    private Date triggerTime;
    private int state;
    private int level;
    private BizCameraVo camera;
    private Integer alarmType;
    private String ivsUrl;
    private String ivsAccount;
    private String ivsPassword;
    private String ivsToken;
}
