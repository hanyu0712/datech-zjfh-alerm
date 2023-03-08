package com.datech.zjfh.alarm.server;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datech.zjfh.alarm.bean.IvsAlarmBean;
import com.datech.zjfh.alarm.bean.IvsDispositionNotificationBean;
import com.datech.zjfh.alarm.bean.IvsImageInfoBean;
import com.datech.zjfh.alarm.bean.IvsImageListBean;
import com.datech.zjfh.alarm.entity.BizAlarmEntity;
import com.datech.zjfh.alarm.mapper.BizAlarmMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Slf4j
@Service
public class BizAlarmServiceImpl extends ServiceImpl<BizAlarmMapper, BizAlarmEntity>{

    public BizAlarmEntity saveAlarm(IvsAlarmBean alarm, Integer lineId, Integer alarmType) {
        try {
            BizAlarmEntity entity = makeEntity(alarm);
            entity.setLineId(lineId);
            entity.setAlarmType(alarmType);
            entity.setCreateTime(new Date());
            this.save(entity);
            return entity;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public BizAlarmEntity makeEntity(IvsAlarmBean alarm) throws Exception {
        IvsDispositionNotificationBean notify =
                alarm.getDispositionNotificationListObject().getDispositionNotificationObject().get(0);
        BizAlarmEntity entity = new BizAlarmEntity();
        entity.setNotificationId(notify.getNotificationID());
//        entity.setLevel(notify.getAlarmLevel());
        entity.setLevel(1);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        entity.setTriggerTime(format.parse(notify.getBehaviorAnalysisObject().getOccuredTime()));
        entity.setCameraCode(notify.getBehaviorAnalysisObject().getDeviceID());
        List<IvsImageListBean> subImageList = notify.getBehaviorAnalysisObject().getSubImageList();
        if (subImageList.size() > 0) {
            IvsImageInfoBean image = subImageList.get(0).getSubImageInfoObject().get(0);
            entity.setImageId(image.getImageID());
        }
        return entity;
    }
}
