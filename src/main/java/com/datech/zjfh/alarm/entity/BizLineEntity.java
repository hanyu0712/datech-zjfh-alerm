package com.datech.zjfh.alarm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("biz_line")
public class BizLineEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "no")
    private String no;

    @TableField(value = "name")
    private String name;

    /**
     *  路线长度
     */
    @TableField(value = "length")
    private Float length;



}
