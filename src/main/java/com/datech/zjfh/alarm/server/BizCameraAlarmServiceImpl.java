package com.datech.zjfh.alarm.server;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datech.zjfh.alarm.entity.BizCameraAlarmEntity;
import com.datech.zjfh.alarm.entity.BizCameraEntity;
import com.datech.zjfh.alarm.entity.BizIvsEntity;
import com.datech.zjfh.alarm.mapper.BizCameraAlarmMapper;
import com.datech.zjfh.alarm.util.BeanCopierUtil;
import com.datech.zjfh.alarm.vo.BizCameraAlarmVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class BizCameraAlarmServiceImpl extends ServiceImpl<BizCameraAlarmMapper, BizCameraAlarmEntity> {
    @Resource
    private SysOrgServiceImpl sysOrgService;

    public BizCameraAlarmEntity add(BizCameraEntity camera) {
        BizCameraAlarmEntity alarm = new BizCameraAlarmEntity();
        alarm.setDetail("摄像头连接异常");
        alarm.setCameraId(camera.getId());
        alarm.setState(0);
        alarm.setCreateTime(new Date());
        this.save(alarm);
        return alarm;
    }

    public BizCameraAlarmVo getVo(BizCameraAlarmEntity entity, BizCameraEntity camera, BizIvsEntity ivs) {
        BizCameraAlarmVo alarm = BeanCopierUtil.copyBean(entity, BizCameraAlarmVo.class);
        alarm.setOrgId(camera.getOrgId());
        alarm.setCameraId(camera.getId());
        alarm.setArea(camera.getArea());
        alarm.setCameraCode(camera.getCode());
        alarm.setCameraName(camera.getName());
        alarm.setCameraIp(camera.getDeviceIp());
        alarm.setOrgName(sysOrgService.getOrgFullName(camera.getOrgId()));
        alarm.setIvsAccount(ivs.getAccount());
        alarm.setIvsPassword(ivs.getPassword());
        alarm.setIvsUrl("https://" + ivs.getIp() + ":18531");
        alarm.setIvsToken(ivs.getToken());
        return alarm;
    }

}
