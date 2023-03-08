package com.datech.zjfh.alarm.handler;

import io.netty.buffer.ByteBuf;

public class MessageBeanEncoder {
    /*public ByteBuf encode(ByteBuf byteBuf, Packet packet) {
        //参考 https://blog.csdn.net/weixin_47301645/article/details/124544519
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        //魔数
        byteBuf.writeInt(MAGIC_NUMBER);
        //版本号
        byteBuf.writeByte(packet.getVersion());
        //序列化算法
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        //指令
        byteBuf.writeByte(packet.getCommand());
        //数据长度
        byteBuf.writeInt(bytes.length);
        //数据
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }*/
}
