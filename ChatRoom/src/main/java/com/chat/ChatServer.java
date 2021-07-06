package com.chat;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class ChatServer {
    //指定服务端端口
    private int port = 8080;
    //构造函数
    public ChatServer(int port){
        this.port = port;
    }
    //服务器启动方法
    public void start() {
        //创建两个线程组
        // EventLoop是处理channel上的IO操作的，一个EventLoop可以管理多个Channel，采用的是多路复用
        // Netty上，一个channel和一个EventLoop绑定后，一般channel上的读和写事件都由同一个EL来处理，目的是只让一个线程操作channel，提高线程安全性
        // EventLoop底层使用的是单个线程的线程池
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // 处理客户端的连接，负责ServerSocketChannel上的 accept事件，
                                                            // ServerSocketChannel只有一个，所以只和一个EventLoop绑定
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // 处理与客户端的通信，即处理数据读写
        // 服务器端的启动器，组装各组件
        ServerBootstrap boot = new ServerBootstrap(); //
        try {
            // 配置服务器,返回一个 channelFuture对象
            ChannelFuture channelFuture = boot.group(bossGroup, workerGroup) // 把两个EventLoopGroup绑定到启动器上，
                    .channel(NioServerSocketChannel.class) // 选择服务器的ServerSocketChannel类型，这里选择的NIO实现的SSChannel
                    .childHandler(new ChannelInitialization()) //核心业务的处理，指定worker(child)的处理操作(Handler),连接建立后都会使用handler
                    // handler规定了worker可以执行哪些操作，单独对它实现；
                    // 可以把ChannelInitializer直接写在这里，理解为对Channel初始化，
                    // 重写initChannel方法，指定用到的Handler
                    .bind(this.port) // 绑定服务器监听端口
                    .sync(); //同步，没有客户端连接时阻塞在这里
            System.out.println("服务器已启动.....");
            // 关闭服务器
            channelFuture.channel().closeFuture().sync();
            System.out.println("服务器关闭");
            //system.out.println(channelFuture);
            //从 channelFuture 中获取 channel
            //Channel channel = channelFuture.channel();
            //System.out.println(channel);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }


    public static void main(String[] args) {
        ChatServer cs = new ChatServer(8080);
        cs.start(); // 启动服务端
    }
}
