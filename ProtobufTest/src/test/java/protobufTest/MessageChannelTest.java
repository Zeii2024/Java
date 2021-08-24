//package protobufTest;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.buffer.ByteBufAllocator;
//import io.netty.channel.*;
//import io.netty.buffer.ByteBuf;
//import io.netty.channel.group.ChannelGroup;
//import io.netty.channel.group.DefaultChannelGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.util.concurrent.GlobalEventExecutor;
//import io.vertx.core.buffer.Buffer;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.InetSocketAddress;
//
//public class MessageChannelTest {
//    public static Channel startServer() throws InterruptedException {
//        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//
//        ServerBootstrap boot = new ServerBootstrap();
//        ChannelFuture channelFuture = boot.group(bossGroup,workerGroup)
//                .channel(NioServerSocketChannel.class)
//                .childHandler(new ChannelInitializer< NioSocketChannel>(){
//
//                    public ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
//
//                    @Override
//                    protected void initChannel(NioSocketChannel sc) throws Exception {
//                        ChannelPipeline pipeline = sc.pipeline();
//                        pipeline.addLast(new ChannelInboundHandlerAdapter(){
////                            Channel channel = sc.read();
//
//                            ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(1024);
//
//                            @Override
//                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                                Channel income = ctx.channel();
//                                System.out.println("msg: "+msg);
//                            }
//                        });
//                    }
//                }).bind(8088).sync();
//
//        Channel channel = channelFuture.channel();
//
//        return channel;
//    }
//
//    public static void startClient(){
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//
//        Bootstrap boot = new Bootstrap();
//        ChannelFuture channelFuture = boot.group(workerGroup) // 绑定 EventLoopGroup
//                .channel(NioSocketChannel.class)
//                .handler(new ChannelInitializer< NioSocketChannel>(){
//
//                    @Override
//                    protected void initChannel(NioSocketChannel sc) throws Exception {
//
//                    }
//                }).connect(new InetSocketAddress("localhost", 8088));
//
//        try {
//            channelFuture.sync();
//            Channel channel = channelFuture.channel();
//
//            // 从系统输入中读取
//            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
//
//            while (true) {
//                channel.writeAndFlush(input.readLine());
//            }
//        } catch (InterruptedException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) throws InterruptedException {
//        // 创建消息对象
//        MessageProtos.Welcome welcome =
//                MessageProtos.Welcome.newBuilder()
//                        .setGreeting(22)
//                        .setContent("Hello!")
//                        .build();
//
//        byte[] welcomeBytes = welcome.toByteArray();
//
//        Channel channel = startServer();
//        startClient();
//
//        System.out.println("channel: "+channel);
//
//        Buffer bufferWrite = Buffer.buffer().appendBytes(welcomeBytes);
//
//        channel.write(bufferWrite);
//
//        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(1024);
//
//        Buffer bufferRead = Buffer.buffer();
//
////        channel.read(buf);
//
//        System.out.println("o: ");
//
//    }
//}
