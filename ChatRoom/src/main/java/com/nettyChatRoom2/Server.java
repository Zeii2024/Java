package com.nettyChatRoom2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.GlobalEventExecutor;


// chat room server
public class Server {
    public Server(){}

    public static void main(String[] args) {
        // 1. 定义两个 EventLoopGroup
        NioEventLoopGroup boss = new NioEventLoopGroup();  // 负责管理连接
        NioEventLoopGroup worker = new NioEventLoopGroup();   // 负责处理读写事件

        // 2.定义启动器
        ServerBootstrap boot = new ServerBootstrap();
        try {
            // 3. 配置启动器 boot, 连接建立前返回 ChannelFuture
            ChannelFuture channelFuture = boot.group(boss, worker)      // 绑定两个 EventLoopGroup
                    .channel(NioServerSocketChannel.class)              // 指定 channel 的类型
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {  // 设置 worker线程对应的 handler
                        // 存放 channels 的容器
                        public ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE); // ???

                        // 要重写 initChannel 方法
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            // 4. 获取 pipeline 并配置 pipeline 的各组件，即具体的 handler
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new MessageEncoder());
                            pipeline.addLast(new MessageDecoder());
                            // 5. 添加自定义的 handler
                            // 连接接入
                            pipeline.addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                                    Channel income = ctx.channel(); // 获取发生事件的 channel
                                    channels.add(income); // 先加入
                                    String msg = "欢迎" + "[" + income.remoteAddress() + "]" + "进入聊天室";
                                    System.out.println(msg);
                                    for (Channel ch : channels) {  // 遍历并广播消息
                                        if (ch != income) {
                                            ch.writeAndFlush(msg);
                                        } else {                    // 给自己发送系统消息
                                            ch.writeAndFlush("[系统消息] " + "您已进入聊天室");
                                        }
                                    }
                                    super.handlerAdded(ctx);
                                }
                            });
                            // 读事件
                            pipeline.addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    Channel income = ctx.channel(); // 获取发生事件的 channel

                                    System.out.println(msg);
                                    for (Channel ch : channels) {  // 遍历并广播消息
                                        //System.out.println(ch+"read");
                                        if (ch != income) {
                                            ch.writeAndFlush("[" + income.remoteAddress() + "]:" + msg);
                                        } else {                   // 给自己发送系统消息
                                            ch.writeAndFlush("[我]:" + msg);
                                        }
                                    }
                                    //super.channelRead(ctx, msg);
                                }
                            });
                            // 连接断开
                            pipeline.addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
                                    Channel income = ctx.channel(); // 获取发生事件的 channel
                                    String msg = "用户" + "[" + income.remoteAddress() + "]" + "离开聊天室";
                                    System.out.println(msg);
                                    for (Channel ch : channels) {  // 遍历并广播消息
                                        if (ch != income) {
                                            ch.writeAndFlush(msg);
                                        }
                                    }
                                    channels.remove(income); // 将断线 channel 移出
                                    //super.handlerRemoved(ctx);
                                }
                                // 异常事件
                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                    System.out.println("通信异常");
                                    //super.exceptionCaught(ctx, cause);
                                    ctx.close();
                                }
                            });
                        }
                    }).bind(8080).sync();  // 6. 绑定端口号,并阻塞主线程，等待连接接入
            System.out.println("服务器已启动...");
            channelFuture.channel().closeFuture().sync(); // 同步关闭 channelFuture
            System.out.println("服务器已关闭...");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 保证工作线程的正确关闭
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}
