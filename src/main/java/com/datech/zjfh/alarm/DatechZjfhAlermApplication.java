package com.datech.zjfh.alarm;

import com.datech.zjfh.alarm.server.SocketNettyServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@MapperScan("com.datech.zjfh.alarm.mapper")
public class DatechZjfhAlermApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(DatechZjfhAlermApplication.class, args);
        SocketNettyServer socketNettyServer = ctx.getBean("socketNettyServer", SocketNettyServer.class);
        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(socketNettyServer);
        service.shutdown();
    }






}
