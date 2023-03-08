package com.datech.zjfh.alarm.handler;

import com.alibaba.fastjson.JSONObject;
import com.datech.zjfh.alarm.bean.IvsResult;
import com.datech.zjfh.alarm.bean.LoginUser;
import com.datech.zjfh.alarm.bean.TcpRequest;
import com.datech.zjfh.alarm.common.constant.CacheConstant;
import com.datech.zjfh.alarm.util.RedisUtil;
import com.datech.zjfh.alarm.util.SocketUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AcceptServerChannelHandler extends ChannelInboundHandlerAdapter {

    //    public static AcceptServerChannelHandler handler;
//    @PostConstruct
//    public void init() {
//        handler = this;
//    }

    private RedisUtil redisUtil;
    public AcceptServerChannelHandler(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //将ByteBuf转换成字符串
        final String json = SocketUtils.getJson(msg);
        log.info("netty接收->"+json);
        try {
            TcpRequest request = JSONObject.parseObject(json, TcpRequest.class);
//            log.info("========netty接收->"+CacheConstant.USER_LOGIN_TOKEN + request.getUserId());
            Object obj = redisUtil.get(CacheConstant.USER_LOGIN_TOKEN + request.getUserId());
            if (obj == null) {
                ctx.writeAndFlush(Unpooled.copiedBuffer(JSONObject.toJSONString(new IvsResult(402, "账号信息异常")), CharsetUtil.UTF_8));
                return;
            }
            LoginUser loginUser = JSONObject.parseObject(obj.toString(), LoginUser.class);
            if (loginUser == null || !loginUser.getToken().equals(request.getToken())) {
                ctx.writeAndFlush(Unpooled.copiedBuffer(JSONObject.toJSONString(new IvsResult(401, "账号已在其他设备登录")), CharsetUtil.UTF_8));
                return;
            }
            if ("createConnect".equals(request.getUrl())) {
                ChannelMap.addChannel(request.getOrgId() + "_" + ctx.channel().remoteAddress(), ctx.channel());
            }
            ctx.writeAndFlush(Unpooled.copiedBuffer(JSONObject.toJSONString(new IvsResult(200, "消息处理成功")), CharsetUtil.UTF_8)); //方法将数据写入并立即发送（刷出）
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            ctx.writeAndFlush(Unpooled.copiedBuffer(JSONObject.toJSONString(new IvsResult(500, "消息处理异常", json)), CharsetUtil.UTF_8));
        }
//        if ("heartBeat".equals(request.getUrl())){
//            ctx.channel().writeAndFlush(json);
//        }

        // 父类该方法内部会调用fireChannelRead
        // 将数据传递给下一个handler
//        super.channelRead(ctx, msg);
        // 调用下一个handler
//        ctx.fireChannelRead(msg);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
//        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("netty有新客户端连接接入->" + ctx.channel().remoteAddress());
    }

    /**
     * 服务端接收客户端发送过来的数据结束之后调用
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //因为缓冲机制，数据被写入到 Channel 中以后，不会立即被发送
        //只有当缓冲满了或者调用了flush()方法后，才会将数据通过 Channel 发送出去
        ctx.flush();
    }

    /**
     * 客户端主动断开服务端的连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("netty 客户端主动断开，ip：{}", ctx.channel().remoteAddress());
        //查询所有在线的客户端
        /*Set keySet = handler.redisUtil.keys("socket_channel_" + "*");
        for (Object key : keySet) {
            Object channelJsonStr = handler.redisUtil.get(key.toString());
            if (channelJsonStr != null) {
                Channel channel = JSONObject.parseObject(channelJsonStr.toString(), Channel.class);
                if (channel.equals(ctx.channel())) {
                    handler.redisUtil.del(key.toString());
                }
            }
        }*/
        ChannelMap.removeChannel(ctx);
        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //心跳配置，一般服务端在心跳配置的时间到达时，如果还没收到客户端的心跳数据，则断开通道。注意：服务端的心跳时间应该比客户端的心跳时间要长
//        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
//            IdleStateEvent event = (IdleStateEvent) evt;
//            if (event.state() == IdleState.READER_IDLE) {
//                log.info("netty 客户端心跳超时，连接断开，ip：{}", ctx.channel().remoteAddress());
//                ctx.close();
//                ChannelMap.removeChannel(ctx);
//            }
//        }
    }

}
