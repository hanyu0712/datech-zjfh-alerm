package com.datech.zjfh.alarm.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.datech.zjfh.alarm.bean.IvsAlarmBean;
import com.datech.zjfh.alarm.bean.IvsResult;
import com.datech.zjfh.alarm.bean.Result;
import com.datech.zjfh.alarm.entity.BizAlarmEntity;
import com.datech.zjfh.alarm.entity.BizCameraEntity;
import com.datech.zjfh.alarm.entity.BizIvsEntity;
import com.datech.zjfh.alarm.entity.BizLineEntity;
import com.datech.zjfh.alarm.handler.ChannelMap;
import com.datech.zjfh.alarm.server.*;
import com.datech.zjfh.alarm.util.BeanCopierUtil;
import com.datech.zjfh.alarm.util.RedisUtil;
import com.datech.zjfh.alarm.vo.BizAlarmVo;
import com.datech.zjfh.alarm.vo.BizCameraVo;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@RestController
@RequestMapping("/ivs")
public class IvsCallbackController {

    @Autowired
    private RedisUtil redisUtil;
    @Resource
    private BizCameraServiceImpl bizCameraService;
    @Resource
    private BizAlarmServiceImpl bizAlarmService;
    @Resource
    private SysOrgServiceImpl sysOrgService;
    @Resource
    private BizLineServiceImpl bizLineService;
    @Resource
    private BizIvsServiceImpl bizIvsService;

    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    public String ivsCallback(@RequestBody String message) {
        log.info("-----ivs callback-----");
        try {
            IvsAlarmBean messageBean = JSONObject.parseObject(message, IvsAlarmBean.class);
            String notificationId = messageBean.getDispositionNotificationListObject().getDispositionNotificationObject().get(0).getNotificationID();
            boolean lockSuccess = redisUtil.lock("alarmLock_" + notificationId, notificationId, 60 * 30);
            if (lockSuccess) {
                String cameraCode = messageBean.getDispositionNotificationListObject().getDispositionNotificationObject().get(0).getBehaviorAnalysisObject().getDeviceID();
                LambdaQueryWrapper<BizCameraEntity> queryWrapper = Wrappers.lambdaQuery();
                queryWrapper.eq(BizCameraEntity::getCode, cameraCode);
                List<BizCameraEntity> cameraList = bizCameraService.list(queryWrapper);
                if (CollectionUtils.isEmpty(cameraList)){
                    log.error("-----camera is null , camera code:{}-----:", cameraCode);
                    return JSONObject.toJSONString(new IvsResult(500, "error"));
                }
                BizCameraEntity camera = cameraList.get(0);
                if (camera.getSubsEnable() == 0) {
                    log.info("-----camera unSubscribe , camera code:{}-----:", cameraCode);
                    return JSONObject.toJSONString(new IvsResult(200, "ok"));
                }
                BizIvsEntity ivs = bizIvsService.getById(camera.getIvsId());
                BizLineEntity line = bizLineService.getById(ivs.getLineId());
                BizAlarmEntity alarm = bizAlarmService.saveAlarm(messageBean, line.getId(), camera.getAlarmType());
                BizAlarmVo vo = BeanCopierUtil.copyBean(alarm, BizAlarmVo.class);
                vo.setIvsAccount(ivs.getAccount());
                vo.setIvsPassword(ivs.getPassword());
                vo.setIvsUrl("https://" + ivs.getIp() + ":18531");
                vo.setIvsToken(ivs.getToken());
                vo.setAlarmName("入侵告警");
                vo.setLevel(1);
                if (line != null) {
                    vo.setLineName(line.getName());
                }
                BizCameraVo cameraVo = getCameraVo(camera);
                vo.setCamera(cameraVo);
                //推送给所有在线的客户端
                ConcurrentHashMap<String, Channel> map = ChannelMap.getChannelHashMap();
                if (map != null) {
                    for (String key : map.keySet()) {
                        log.info("--------Channel map orgId key : {} -------", key);
                        Channel channel = ChannelMap.getChannelByKey(key);
                        if (channel != null) {
                            log.info("-------- flush alarm : {} -------", JSONObject.toJSONString(vo));
                            channel.writeAndFlush(Unpooled.copiedBuffer("alarm$:" + JSONObject.toJSONString(vo), CharsetUtil.UTF_8));
                        } else {
                            log.info("--------channel is null, orgId key:{}  -------", key);
                        }
                    }
                } else {
                    log.info("--------long connection is null-------");
                }
            } else {
                log.info("------can not get lock , notificationId:{}", notificationId);
            }
            return JSONObject.toJSONString(new IvsResult(200, "ok"));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发生异常，信息：{}", e.getMessage());
            return JSONObject.toJSONString(new IvsResult(500, "error"));
        }
    }

    private BizCameraVo getCameraVo(BizCameraEntity camera) {
        BizCameraVo cameraVo = BeanCopierUtil.copyBean(camera, BizCameraVo.class);
        cameraVo.setOrgName(sysOrgService.getOrgFullName(camera.getOrgId()));
        return cameraVo;
    }

    @RequestMapping(value = "/getChannelMap", method = RequestMethod.GET)
    public Result<Object> getChannelMap() {
        log.info("-----getChannelMap-----:");
        try {
            AtomicReference<String> result = new AtomicReference<>("");
            ConcurrentHashMap<String, Channel> channelHashMap = ChannelMap.getChannelHashMap();
            if (!channelHashMap.isEmpty()) {
                channelHashMap.keySet().forEach(k -> {
                    log.info("channel map key:{}", k);
                    result.getAndUpdate(v -> v + ";" + k);
                });
            }
            return Result.OK(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发生异常，信息：{}", e.getMessage());
            return Result.error("异常：" + e.getMessage());
        }

    }




}
