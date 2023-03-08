package com.datech.zjfh.alarm.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
public class SimpleHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) {
        try {
            log.info("SimpleHttpServerHandler receive fullHttpRequest=" + fullHttpRequest);

            configureTasks();
            String result = doHandle(fullHttpRequest);
            log.info("SimpleHttpServerHandler,result=" + result);
            byte[] responseBytes = result.getBytes(StandardCharsets.UTF_8);
            JSONObject obj = new JSONObject();
            obj.put("resultCode", 0);

            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(obj.toString().getBytes()));
            response.headers().set("Content-Type", "text/html; charset=utf-8");
            response.headers().setInt("Content-Length", response.content().readableBytes());

            boolean isKeepAlive = HttpUtil.isKeepAlive(response);
            if (!isKeepAlive) {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set("Connection", "keep-alive");
                ctx.write(response);
            }
        } catch (Exception e) {
            log.error("channelRead0 exception,", e);
        }
    }

    public void configureTasks() {
        log.info("task");
        /*if(ChannelMap.getChannelHashMap()!=null && ChannelMap.getChannelHashMap().size()>0){
            Iterator<Map.Entry<String, Channel>> iterator = ChannelMap.getChannelHashMap().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Channel> next = iterator.next();
                String key = next.getKey();
                Channel channel = next.getValue();
                if (channel.isActive()) {
                    // 指令发送
                    ByteBuf bufff = Unpooled.buffer();
                    // 根据具体业务传输数据
                    bufff.writeBytes(key.getBytes(StandardCharsets.UTF_8));

                    channel.writeAndFlush(bufff).addListener((ChannelFutureListener) future -> {
                        StringBuilder sb = new StringBuilder();
                        if(!StringUtils.isEmpty(key)){
                            sb.append("【").append(key).append("】");
                        }
                        if (future.isSuccess()) {
                            System.out.println(sb.toString()+"回写成功"+key);
                        } else {
                            System.out.println(sb.toString()+"回写失败"+key);
                        }
                    });
                } else {
                    ChannelMap.getChannelHashMap().remove(key);

                }
            }
        }*/
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * 实际处理
     *
     * @param fullHttpRequest HTTP请求参数
     * @return
     */
    private String doHandle(FullHttpRequest fullHttpRequest) {
        if (HttpMethod.GET == fullHttpRequest.method()) {
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(fullHttpRequest.uri());
            Map<String, List<String>> params = queryStringDecoder.parameters();
            return JSON.toJSONString(params);
        } else if (HttpMethod.POST == fullHttpRequest.method()) {
            byte[] bytes = new byte[fullHttpRequest.content().readableBytes()];
            fullHttpRequest.content().readBytes(bytes);
            return new String(bytes, CharsetUtil.UTF_8);
        }

        return fullHttpRequest.method().name();
    }
}

