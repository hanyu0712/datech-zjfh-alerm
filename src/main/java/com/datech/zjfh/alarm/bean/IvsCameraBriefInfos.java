package com.datech.zjfh.alarm.bean;

import com.datech.zjfh.alarm.entity.BizCameraEntity;
import lombok.Data;

import java.util.List;

@Data
public class IvsCameraBriefInfos {
    private List<BizCameraEntity> cameraBriefInfoList;
}
