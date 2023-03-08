package com.datech.zjfh.alarm.server;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.datech.zjfh.alarm.common.ivs.GetSubDeviceList;
import com.datech.zjfh.alarm.entity.BizCameraAlarmEntity;
import com.datech.zjfh.alarm.entity.BizCameraEntity;
import com.datech.zjfh.alarm.entity.BizIvsEntity;
import com.datech.zjfh.alarm.handler.ChannelMap;
import com.datech.zjfh.alarm.vo.BizCameraAlarmVo;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@EnableScheduling
@Configuration
public class IvsCameraOfflineTask {

    @Autowired
    private BizCameraAlarmServiceImpl bizCameraAlarmService;
    @Resource
    private BizCameraServiceImpl bizCameraService;
    @Resource
    private BizIvsServiceImpl bizIvsService;

    @Scheduled(cron = "17 0/2 * * * ?")
    public void configureTasks() {
        log.info("=========Ivs Camera Offline Task");
        try {
            // 查询IVS摄像头
            List<BizIvsEntity> ivsList = bizIvsService.list();
            for (BizIvsEntity ivs : ivsList) {
                List<BizCameraEntity> cameraIvsList = GetSubDeviceList.getSubDeviceList("https://" + ivs.getIp() + ":18531", ivs.getToken());
                if (cameraIvsList != null && cameraIvsList.size() > 0) {
                    List<BizCameraEntity> databaseList = bizCameraService.list();
                    Map<String, BizCameraEntity> databaseMap = databaseList.stream().collect(Collectors.toMap(BizCameraEntity::getCode, Function.identity()));
                    for (BizCameraEntity cameraIvs : cameraIvsList) {
                        //离线摄像头
                        if (cameraIvs.getStatus() == 0 || cameraIvs.getStatus() == 2) {
                            BizCameraEntity camera = databaseMap.get(cameraIvs.getCode());
                            if (camera != null && camera.getStatus() == 1) {
                                //更新离线
                                BizCameraEntity entityUpdate = new BizCameraEntity();
                                entityUpdate.setStatus(cameraIvs.getStatus());
                                LambdaUpdateWrapper<BizCameraEntity> updateWrapper = new LambdaUpdateWrapper<>();
                                updateWrapper.eq(BizCameraEntity::getCode, cameraIvs.getCode());
                                bizCameraService.update(entityUpdate, updateWrapper);
                                //生成设备离线告警
                                BizCameraAlarmEntity alarm = bizCameraAlarmService.add(camera);
                                BizCameraAlarmVo vo = bizCameraAlarmService.getVo(alarm, camera, ivs);
                                //推送给所有在线的客户端
                                ConcurrentHashMap<String, Channel> map = ChannelMap.getChannelHashMap();
                                if (map != null) {
                                    for (String key : map.keySet()) {
                                        Channel channel = ChannelMap.getChannelByKey(key);
                                        if (channel != null) {
                                            channel.writeAndFlush(Unpooled.copiedBuffer("deviceAlarm$:" + JSONObject.toJSONString(vo), CharsetUtil.UTF_8));
                                        }
                                    }
                                }
                            }
                        } else if (cameraIvs.getStatus() == 1) {
                            BizCameraEntity camera = databaseMap.get(cameraIvs.getCode());
                            if (camera != null && camera.getStatus() != 1) {
                                //更新在线
                                BizCameraEntity entityUpdate = new BizCameraEntity();
                                entityUpdate.setStatus(1);
                                LambdaUpdateWrapper<BizCameraEntity> updateWrapper = new LambdaUpdateWrapper<>();
                                updateWrapper.eq(BizCameraEntity::getCode, cameraIvs.getCode());
                                bizCameraService.update(entityUpdate, updateWrapper);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
