package vertxNet;

import handlers.ProtoHandler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import messages.Messages;


public class Client {
    static String host = "127.0.0.1";
    static int port = 4321;
    static Vertx vertx = Vertx.vertx();

    public static void main(String[] args) {
        // 客户端设置
        NetClientOptions clientOptions =
                new NetClientOptions()
                .setConnectTimeout(5000)
                .setTcpNoDelay(false)
                .setSsl(false);
        // 创建客户端
        NetClient client = vertx.createNetClient(clientOptions);

        Messages.Welcome welcome = Messages.Welcome.newBuilder()
                .setGreeting(2048)
                .setContent("How are you bro?")
                .build();

        // 连接
        client.connect(port, host, res -> {
           if(res.succeeded()){
               System.out.println("Connected!");
               // 获取 socket
               NetSocket socket = res.result();

               ProtoHandler protoHandler = new ProtoHandler();
               socket.handler(protoHandler); // 指定客户端的 handler

               Buffer buffer = Buffer.buffer();
               // 向 buffer 中写入消息对象
               try {
                   protoHandler.writeBuffer(buffer, welcome);
               } catch (Exception e) {
                   e.printStackTrace();
               }

               socket.write(buffer);
           } else{
               System.out.println("Failed to connect: " + res.cause().getMessage());
           }
        });
    }
}
