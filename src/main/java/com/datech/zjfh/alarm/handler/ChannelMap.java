package com.datech.zjfh.alarm.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentHashMap;

public class ChannelMap {
    public static int channelNum = 0;
    private static ConcurrentHashMap<String, Channel> channelHashMap = null;

    public static ConcurrentHashMap<String, Channel> getChannelHashMap() {
        return channelHashMap;
    }

    public static Channel getChannelByKey(String key) {
        if (channelHashMap == null || channelHashMap.isEmpty()) {
            return null;
        }
        return channelHashMap.get(key);
    }

    public static void addChannel(String key, Channel channel) {
        if (channelHashMap == null) {
            channelHashMap = new ConcurrentHashMap<String, Channel>(32);
        }
        channelHashMap.put(key, channel);
        channelNum++;
    }

    public static void removeChannel(ChannelHandlerContext ctx) {
        for( String key :channelHashMap.keySet()){
            if (channelHashMap.get(key) != null && channelHashMap.get(key).equals(ctx.channel())) {
                channelHashMap.remove(key);
            }
        }
        /*if (channelHashMap.containsKey(name)) {
            channelHashMap.remove(name);
            return 0;
        } else {
            return 1;
        }*/
    }
}
