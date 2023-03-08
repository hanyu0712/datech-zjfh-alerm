package com.datech.zjfh.alarm.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeartBeatServerHandler extends SimpleChannelInboundHandler<String> {

    private int lossConnectCount = 0;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                lossConnectCount++;
                log.info("未收到客户端的消息了！count:" + lossConnectCount);
                if (lossConnectCount > 2) {
                    log.info("关闭这个不活跃通道！");
                    ctx.channel().close();
                }
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        lossConnectCount = 0;
        log.info("heartbeat receive :" + s);
        if ("ping$_".equals(s)) {
            ctx.writeAndFlush("pong");
        } else {
            ctx.channel().writeAndFlush(s);
        }
    }
//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        super.channelReadComplete(ctx);
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
