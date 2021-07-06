package com.chat;

import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleToIntFunction;

/*
* WorkerHandler决定了workerGroup具体可以执行的操作，它相当于一个Handler的管理器
* 它要继承自ChannelInitializer，这里指定输入的泛型是NioSocketChannel
* 也就是说，Handler用来操作传入的SocketChannel
* 不要在一个 handler 里写那么多方法，一个方法写一个 handler,利用 super 把信息传递给下一个 handler
*
* 无论是服务端还是客户端，数据的处理都要经过Handler，handler是通信业务的核心
* 字符串数据先从Channel由StringEncoder转化为ByteBuf，然后发给服务器
* 服务器收到消息，由某个EventLoop处理read事件，接收到ByteBuf，再经由StringDecoder解码为字符串
* 实际上就是把NIO做了封装
* */
public class ChannelInitialization extends ChannelInitializer<NioSocketChannel> {
    // 存放连接 channel 的队列，一定要注意这个 channels 的作用范围，要定义在类里，类变量
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    // 需要重写initChannel方法，将具体操作定义在这个方法内
    @Override  // 连接建立后，调用initChannel方法
    protected void initChannel(NioSocketChannel sc) throws Exception {
        // 客户端连接后,打印出来
        System.out.println("客户端接入："+sc.remoteAddress());

        // 添加具体用到的Handler
        //定义字符串的拆装包方式.指定特写的分隔符进行装包拆包，长度为8192，分隔符为换行符
        //sc.pipeline().addLast("framer",new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()))
        sc.pipeline().addLast("decoder",new StringDecoder()); // 加入字符串解码器，把ByteBuf转化为字符串
        sc.pipeline().addLast("encoder",new StringEncoder()); // 加入字符串编码器，把字符串转化为ByteBuf

        //new ChannelInboundHandler(){}
        sc.pipeline().addLast("handler",new ChannelInboundHandlerAdapter() { // 自定义handler,里面可以重写很多的方法
            //List<Channel> channels = new ArrayList<>();
            //新连接加入队列
            @Override
            public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                // 新 channel 连接进来
                Channel income = ctx.channel();
                //向其他人广播，有新用户上线. 遍历
                for(Channel ch : channels){
                    if(ch != income){
                        //用 writeAndFlush 向 channel 中写入消息即可
                        ch.writeAndFlush("[系统消息]：" + income.remoteAddress() +"已上线");
                    }
                }
                channels.add(income);
                for(Channel c : channels){
                    System.out.println(c.remoteAddress()+"在线");
                }
            }
            //离线连接移出队列
            @Override
            public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
                // channel 断开连接
                Channel outcome = ctx.channel();
                //向其他人广播，用户离线. 遍历
                for(Channel ch : channels){
                    if(ch != outcome){
                        //用 writeAndFlush 向 channel 中写入消息即可
                        ch.writeAndFlush("[系统消息]：" + outcome.remoteAddress() +"已离线");
                    }else{
                        System.out.println(ch.remoteAddress()+"已离线..");
                    }
                }
                channels.remove(outcome);
            }

            // 注册
//            @Override
//            public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
//                super.channelRegistered(ctx);
//                // 新 channel 连接进来
//                Channel income = ctx.channel();
//                //向其他人广播，有新用户上线. 遍历
//                for(Channel ch : channels){
//                    if(ch != income){
//                        //用 writeAndFlush 向 channel 中写入消息即可
//                        ch.writeAndFlush("[系统消息]：" + income.remoteAddress() +"已上线");
//                    }
//                }
//                channels.add(income);
//                for(Channel c : channels){
//                    System.out.println(c);
//                }
//            }
            // 在线状态
//            @Override
//            public void channelActive(ChannelHandlerContext ctx) throws Exception {
//                 System.out.println(ctx.channel().remoteAddress()+":在线中");
//            }
            // 离线状态
//            @Override
//            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//                System.out.println(ctx.channel().remoteAddress()+" 已离线");
//            }
            // 读事件，读取channel传入的数据
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                //收到的数据也要转发给其他人，遍历
                Channel income = ctx.channel();
                //将所有消息在服务台输出一遍
                System.out.println(income.remoteAddress()+"说："+msg);
                //向其他人广播消息
                for(Channel ch : channels){
                    int i = 0;
                    if(ch != income){
                        System.out.println(i++);
                        //用 writeAndFlush 向 channel 中写入消息即可
                        ch.writeAndFlush(income.remoteAddress() +"说：" + msg);
                    }else{ //自己的消息转发给自己
                        ch.writeAndFlush("我说："+msg);
                    }
                }
            }
            // 读取成功事件
            @Override
            public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                super.channelReadComplete(ctx);
//                System.out.println(ctx.channel().remoteAddress()+" 消息已读");
            }
            //离线事件
//            @Override
//            public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
//                super.channelUnregistered(ctx);
//                // channel 断开连接
//                Channel outcome = ctx.channel();
//                //向其他人广播，用户离线. 遍历
//                for(Channel ch : channels){
//                    if(ch != outcome){
//                        //用 writeAndFlush 向 channel 中写入消息即可
//                        ch.writeAndFlush("[系统消息]：" + outcome.remoteAddress() +"已离线");
//                    }
//                }
//                channels. remove(outcome);
//            }

            // 异常事件
            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                System.out.println("通信异常");
                super.exceptionCaught(ctx, cause);
                ctx.close();
            }
        });

    }
}
