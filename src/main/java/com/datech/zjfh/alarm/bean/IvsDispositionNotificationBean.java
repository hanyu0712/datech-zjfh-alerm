package com.datech.zjfh.alarm.bean;

import lombok.Data;

@Data
public class IvsDispositionNotificationBean {
    private IvsBehaviorAnalysisBean BehaviorAnalysisObject;
    private String NotificationID;
//    private String AlarmDetailReason;
    private int AlarmLevel;
//    private String AlarmRuleType;
//    private String TriggerTime;
}
