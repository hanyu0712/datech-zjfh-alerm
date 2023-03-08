package com.datech.zjfh.alarm.bean;

import lombok.Data;

import java.util.List;

@Data
public class IvsBehaviorAnalysisBean {
    private List<IvsImageListBean> SubImageList;
    private String DeviceID;
    private String OccuredTime;
}
