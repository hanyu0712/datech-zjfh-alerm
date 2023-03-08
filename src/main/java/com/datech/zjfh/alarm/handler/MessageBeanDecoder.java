package com.datech.zjfh.alarm.handler;

import com.datech.zjfh.alarm.bean.MessageBean;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MessageBeanDecoder extends ReplayingDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int length = in.readInt();

        byte[] content = new byte[length];
        in.readBytes(content);

        MessageBean messageProtocol = new MessageBean();
//        messageProtocol.setLen(length);
//        messageProtocol.setContent(content);
        out.add(messageProtocol);
    }
}
