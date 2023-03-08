package com.datech.zjfh.alarm.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class SocketUtils {
    //将ByteBuf消息转换成字符串
    public static String getJson(Object msg) {
        String json;
        try {
            ByteBuf buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            json = new String(bytes);
        } finally {
            ReferenceCountUtil.release(msg);
        }
        return json;

    }
    //当收到消息后，进行相关业务逻辑的处理，处理完后将结果发生给对方
    public static void sendMsg(ChannelHandlerContext ctx, String msg){
        ctx.writeAndFlush(Unpooled.buffer().writeBytes(msg.getBytes()));
    }

    //服务端或客户端，主动向对方发生消息，如果是服务端，则需要将客户端注册成功后的channel保存下来
    //如服务端需要向所有客户端广播某个消息时
    public static void sendMsg(Channel ctx, String msg){
        ctx.writeAndFlush(Unpooled.buffer().writeBytes(msg.getBytes()));
    }
//————————————————
//    版权声明：本文为CSDN博主「cuit_618」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
//    原文链接：https://blog.csdn.net/zhaocuit/article/details/121186246
}
