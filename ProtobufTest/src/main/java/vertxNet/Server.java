package vertxNet;

import handlers.ProtoHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.impl.NetSocketInternal;
import messages.Messages;

import java.nio.ByteOrder;

public class Server {
    static String host = "127.0.0.1";
    static int port = 4321;
    static Vertx vertx = Vertx.vertx();

    public Server(){ }

    public static void handler(NetSocket netSocket){
        // 把 netSocket 转换为 netSocketInternal，然后获取它的 pipeline
        // 向 pipeline 里加入需要的处理器，比如 Encoder 和 Decoder
        NetSocketInternal soi = (NetSocketInternal) netSocket;
        ChannelPipeline pipeline = soi.channelHandlerContext().pipeline();

        // 添加LengthFieldBasedFrameDecoder解码器
        // 其作用是，先读取 Length，判断消息长度是否等于 Length，如果是则进行下一步，
        // 如果不是，说明消息没有全部到达，继续等待消息。目的是解决粘包和半包问题
        LengthFieldBasedFrameDecoder decoder =
                new LengthFieldBasedFrameDecoder(ByteOrder.BIG_ENDIAN, Integer.MAX_VALUE,   // 消息长度 + 消息体是很常用的格式
                0,4,-4,0,true){
                    @Override
                    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
                        return super.decode(ctx, in);
                    }
                };
        pipeline.addBefore("handler","DECODER", decoder);

        // 把 netSocket 接收到的消息交给 protoHandler 来处理
        ProtoHandler protoHandler = new ProtoHandler();
        netSocket.handler(protoHandler);


        // 向 socket 写数据
        Messages.Talk talk = Messages.Talk.newBuilder()
                .setName("Server")
                .setContent("Now we can talk")
                .build();

        Buffer buffer = Buffer.buffer();
        // 消息要通过 protoBuffer.writeBuffer 写入到 buffer：4+4+length
        try {
            protoHandler.writeBuffer(buffer,talk);
        } catch (Exception e) {
            e.printStackTrace();
        }

        netSocket.write(buffer);
    }

    public static void main(String[] args) {

        NetServerOptions serverOptions =
                new NetServerOptions()
                        .setHost(host)  // 服务器地址
                        .setPort(port)  // 服务器端口
                        .setTcpNoDelay(false)  // Tcp no delay
                        .setSsl(false)
                        .setLogActivity(true);  // 日志
        NetServer server = vertx.createNetServer(serverOptions);

        // 设置 connect handler，这一步要先进行
        server.connectHandler(Server::handler);  // 返回值是一个 netSocket，将其直接传给 handler

        // 监听
        server.listen(res -> {
            if(res.succeeded()) System.out.println("Server is listening at " + port);
            else System.out.println("Failed to bind!");
        });  // 也可以在 listen 中指定 host 和 port
    }
}
