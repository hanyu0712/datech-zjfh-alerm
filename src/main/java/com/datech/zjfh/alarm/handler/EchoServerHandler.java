package com.datech.zjfh.alarm.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        ByteBuf in = (ByteBuf) msg;
        log.info("Server received: " + in.toString(CharsetUtil.UTF_8));
        ctx.writeAndFlush(in);
    }

//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx){
//        ctx.writeAndFlush(ChannelFutureListener.CLOSE);
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}
