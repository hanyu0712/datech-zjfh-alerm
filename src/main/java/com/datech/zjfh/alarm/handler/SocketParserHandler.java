package com.datech.zjfh.alarm.handler;

import com.datech.zjfh.alarm.SocketUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class SocketParserHandler extends ByteToMessageDecoder {
    /**
     * WebSocket握手的协议前缀
     */
    private static final String WEBSOCKET_PREFIX = "GET /";
    private final Integer BASE_LENGTH = 14;
    int beginIndex = 0;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        String protocol = getBufStart(in);
        in.resetReaderIndex();
        /*if (protocol.startsWith(WEBSOCKET_PREFIX)) {
            //websocket协议本身是基于http协议的，所以这边也要使用http解编码器
            ctx.pipeline().addLast(new HttpServerCodec());
            //以块的方式来写的处理器
            ctx.pipeline().addLast(new ChunkedWriteHandler());
            ctx.pipeline().addLast(new HttpObjectAggregator(8192));
            ctx.pipeline().addLast(new WebSocketHandler());
            ctx.pipeline().addLast(new WebSocketServerProtocolHandler("/ws", null, true, 65536 * 10));
            //去除socket处理
            ctx.pipeline().remove(NettySocketHandler.class);
            ctx.pipeline().remove(this.getClass());
        } else {*/
            ByteBuf byteBuf = in.readerIndex(beginIndex);
            int readableBytes = byteBuf.readableBytes();
            if (readableBytes >= BASE_LENGTH) {
                // 防止socket字节流攻击
                // 防止，客户端传来的数据过大
                // 因为，太大的数据，是不合理的
                if (readableBytes > 2048) {
                    byteBuf.skipBytes(readableBytes);
                }

                while (byteBuf.readableBytes() > 0) {
                    int thisReadableBytes = byteBuf.readableBytes();
                    byte[] bytes = new byte[thisReadableBytes];
                    byteBuf.readBytes(bytes);
                    log.info("send body: " + new String(bytes));

                    // 消息的长度
                    int length = SocketUtils.read_int_le(bytes, 0);
                    log.info("readableBytes: " + readableBytes + "\t custom decode msg length: " + length);

                    // 判断请求数据包数据是否到齐
                    if (thisReadableBytes < length) {
                        // 还原读指针
                        in = byteBuf.readerIndex(beginIndex);
                        return;
                    }
                    byteBuf.resetReaderIndex();
                    //bytebuff在封装tcp流信息时，前面会多加4位，作为整个消息的长度
                    byte[] msgBytes = new byte[length + 4];
                    byteBuf.readBytes(msgBytes);
                    beginIndex = byteBuf.readerIndex();
                    String parse = SocketUtils.parse(msgBytes);
                    log.info("full bag body: " + parse);
                    out.add(Unpooled.copiedBuffer(msgBytes));
                    byteBuf.markReaderIndex();
                }
                beginIndex = 0;
            }
//        }

    }

    private String getBufStart(ByteBuf in) {
        int length = in.readableBytes();
        // 标记读位置
        in.markReaderIndex();
        byte[] content = new byte[length];
        in.readBytes(content);
        return new String(content);
    }

}
