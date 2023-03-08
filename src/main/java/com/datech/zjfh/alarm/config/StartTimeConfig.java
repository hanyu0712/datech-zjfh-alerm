package com.datech.zjfh.alarm.config;


import com.datech.zjfh.alarm.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
@Order(value = 1)
public class StartTimeConfig implements ApplicationRunner {
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void run(ApplicationArguments args){
        log.info("======StartTimeConfig ");
        redisUtil.set("startTime", new Date().getTime());
    }

}
