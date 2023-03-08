package com.datech.zjfh.alarm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("biz_camera_alarm")
public class BizCameraAlarmEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     *  组织节点 ID
     */
    @TableField(value = "org_id")
    private Integer orgId;
    /**
     *  摄像头 ID
     */
    @TableField(value = "camera_id")
    private Integer cameraId;
    /**
     *  组织节点全称
     */
    @TableField(value = "org_name")
    private String orgName;
    /**
     *  详情
     */
    @TableField(value = "detail")
    private String detail;
    /**
     *  里程
     */
    @TableField(value = "area")
    private String area;

    /**
     *  名称
     */
    @TableField(value = "camera_name")
    private String cameraName;

    /**
     *  编码
     */
    @TableField(value = "camera_code")
    private String cameraCode;

    /**
     *  IP 地址
     */
    @TableField(value = "camera_ip")
    private String  cameraIp;
    /**
     * 设备状态
     */
    @TableField(value = "camera_status")
    private Integer  cameraStatus;

    /**
     *  状态，0：未处理，1：已处理，2：忽略
     */
    @TableField(value = "state")
    private Integer state;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 解除时间
     */
    @TableField(value = "clear_time")
    private Date clearTime;

    /**
     *  解除人
     */
    @TableField(value = "clear_user")
    private String clearUser;

    /**
     *  处理意见
     */
    @TableField(value = "opinions")
    private String opinions;
}
