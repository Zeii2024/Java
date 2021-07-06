package com.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.NettyRuntime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;


public class ChatClient {
    private String host;
    private int port;

    public ChatClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    public void start() throws InterruptedException {
        // 这里客户端的消息读写也用EventLoop来实现，只需要一个EventLoop
        EventLoopGroup worker = new NioEventLoopGroup();
        // 客户端启动器
        Bootstrap boot = new Bootstrap();
        try {
            // 设置启动器
            boot.group(worker) // 绑定EventLoop组
                    .channel(NioSocketChannel.class) // 指定Channel类型为NioSocketChannel
                    .handler(new ChannelInitializer<NioSocketChannel>() { // 初始化Channel，指定Handler，这里直接写
                        @Override // 重写initChannel方法，加入具体的Handler
                        protected void initChannel(NioSocketChannel sc) throws Exception { // 同样是在连接建立后执行
                            sc.pipeline().addLast(new StringEncoder()); // 字符串编码器
                            sc.pipeline().addLast(new StringDecoder()); // 字符串解码器
                            sc.pipeline().addLast(new ChannelInboundHandlerAdapter() { // 自定义Handler
                                // Handler分为Inbound(入站，处理数据流入)和Outbound(出站，处理数据流出)
                                @Override // 读事件
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    System.out.println(msg);
                                }
                            });
                        }
                    });

            Channel channel = boot.connect(new InetSocketAddress("localhost", 8080)) // 连接到服务器
                    .sync() // 同步，阻塞方法，直到连接建立才能继续执行。利用阻塞方法来等待线程的建立
                    .channel(); // 代表与服务器的连接对象，由这个channel来读写数据
            // 建立连接后，channel 开始工作
            // 从系统IO 读取数据到  BufferedReader input 中
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            // 轮询发送消息
            while (true) {
                channel.writeAndFlush(input.readLine()); // 以换行符结尾，用于字符串分割
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally { // 最后要关闭 worker 线程
            worker.shutdownGracefully();
        }
        //如何关闭线程
        // 获取 channel 的 closeFuture，利用 closeFuture 的回调来处理 channel 关闭后的事务
//            ChannelFuture closeFuture = channel.closeFuture();
        //交给一个 Listener 来异步处理
        // closeFuture.addListener(new ChannelFutureListener(){
//        @Override
//        public void operationComplete (ChannelFuture future) throws Exception {
//            //处理关闭 channel 之后的操作
//            group.shutdownGracefully();
//        }
//    });


    }

    public static void main(String[] args) {
        // 启动客户端
        ChatClient cc = new ChatClient("localhost",8080);
        // System.out.println(NettyRuntime.availableProcessors()); // 输出可用的核心数
        try {
            cc.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
