package com.datech.zjfh.alarm.server;

import com.datech.zjfh.alarm.handler.AcceptServerChannelHandler;
import com.datech.zjfh.alarm.util.RedisUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component("socketNettyServer")
public class SocketNettyServer implements Runnable {


    @Autowired
    private RedisUtil redisUtil;

    @SneakyThrows
    @Override
    public void run() {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss,work)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,2048)
                    //开启tcp nagle算法
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    //开启长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast(new StringDecoder());
//                            ch.pipeline().addLast(new StringEncoder());
//                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.wrappedBuffer(delimiter.getBytes())));
//                            ch.pipeline().addLast(new DelimiterBasedMessageEncoder(delimiter));
//                            ch.pipeline().addLast("timeout", new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS));
//                            ch.pipeline().addLast(new HeartBeatServerHandler());
//                            ch.pipeline().addLast(new MessageBeanEncoder())
//                            ch.pipeline().addLast(new MessageBeanDecoder());
                            ch.pipeline().addLast("acceptServerChannelHandler", new AcceptServerChannelHandler(redisUtil));
//                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    });
            ChannelFuture future = b.bind(9016).sync();
            log.info(String.format("socket netty server started!!!! port: %d", 9016));
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            work.shutdownGracefully().sync();
            boss.shutdownGracefully().sync();
        }
    }

}
