package com.datech.zjfh.alarm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_org")
public class SysOrgEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     *  层级
     */
    @TableField(value = "level")
    private Integer level;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 操作人姓名
     */
    @TableField(value = "pid")
    private Integer pid;

    /**
     * 负责人
     */
    @TableField(value = "responsible")
    private String responsible;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
