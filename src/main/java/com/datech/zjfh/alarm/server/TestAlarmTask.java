package com.datech.zjfh.alarm.server;

import io.netty.util.CharsetUtil;
import com.datech.zjfh.alarm.handler.ChannelMap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@EnableScheduling
@Configuration
public class TestAlarmTask {

    @Resource
    private BizCameraServiceImpl bizCameraService;
//    @Scheduled(cron = "4 0/1 * * * ?")
    public void configureTasks() throws Exception {
        log.info("====TestAlarmTask");
        ConcurrentHashMap<String, Channel> map = ChannelMap.getChannelHashMap();
        for (String key : map.keySet()) {
            Channel channel = ChannelMap.getChannelByKey(key);
            if (channel != null) {
                channel.writeAndFlush(Unpooled.copiedBuffer("alarm$:{\"alarmName\":\"入侵告警\",\"camera\":{\"area\":\"k1-k2\",\"code\":\"08797026540763120101\",\"createTime\":1662628659000,\"deviceIp\":\"192.168.2.120\",\"id\":24,\"name\":\"192.168.2.120\",\"orgId\":52,\"orgName\":\"南宁市铁路总局南宁机务段南宁车间1南宁车间1工区1\",\"status\":1},\"id\":804,\"imageId\":\"4478218802\",\"level\":1,\"state\":0,\"triggerTime\":" + new Date().getTime() + "}", CharsetUtil.UTF_8));
            }
        }
    }
}
