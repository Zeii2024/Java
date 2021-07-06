package com.nettyChatRoom2;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;


public class Client {
    public Client(){}

    public static void main(String[] args) {
        // 1. 创建 EventLoopGroup
        EventLoopGroup worker = new NioEventLoopGroup();

        // 2. 定义并配置启动器
        Bootstrap boot = new Bootstrap();
        ChannelFuture channelFuture = boot.group(worker) // 绑定 EventLoopGroup
                .channel(NioSocketChannel.class)         // 指定 channel 类型
                .handler(new ChannelInitializer<NioSocketChannel>() {  // 设置 handler
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 获取 pipeline
                        ChannelPipeline pipeline = ch.pipeline();
                        // 3. 添加具体的 handler
                        pipeline.addLast(new StringDecoder());  // 字符串解码器
                        pipeline.addLast(new StringEncoder());  // 字符串编码器
                        pipeline.addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);
                                //super.channelRead(ctx, msg);
                            }
                        });
                    }
                }).connect(new InetSocketAddress("localhost", 8080)); // 连接

        try{
            // 采用 sync()同步方案
            channelFuture.sync();
            Channel channel = channelFuture.channel();
            // 从系统输入中读取
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                channel.writeAndFlush(input.readLine());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            channelFuture.channel().closeFuture();
        }

        /*
        // 4. 利用 channelFuture 的 addListener 方法进行异步处理结果 // 行不通！！！
        channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {

            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                System.out.println("连接已建立...");
                Channel channel = channelFuture.channel();
                // 从系统输入中读取
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

                while (true) {
                    channel.writeAndFlush(input.readLine());
                }
            }
        });*/
    }
}
