package com.datech.zjfh.alarm.config;

import com.datech.zjfh.alarm.mqtt.MyMQTTClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Data
@Slf4j
@Configuration
public class MqttConfiguration {

//    @Value("${mqtt.host}")
    String host;
//    @Value("${mqtt.username}")
    String username;
//    @Value("${mqtt.password}")
    String password;
//    @Value("${mqtt.clientId}")
    String clientId;
//    @Value("${mqtt.timeout}")
    int timeOut;
//    @Value("${mqtt.keepalive}")
    int keepAlive;
//    @Value("${mqtt.topic1}")
    String topic1;

//    @Bean//注入spring
    public MyMQTTClient myMQTTClient() {
        MyMQTTClient myMQTTClient = new MyMQTTClient(host, username, password, clientId, timeOut, keepAlive);
        for (int i = 0; i < 10; i++) {
            try {
                myMQTTClient.connect();
                //不同的主题
                //   myMQTTClient.subscribe(topic1, 1);
                //   myMQTTClient.subscribe(topic2, 1);
                //   myMQTTClient.subscribe(topic3, 1);
                //   myMQTTClient.subscribe(topic4, 1);
                return myMQTTClient;
            } catch (MqttException e) {
                log.error("MQTT connect exception,connect time = " + i);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return myMQTTClient;
    }
}
