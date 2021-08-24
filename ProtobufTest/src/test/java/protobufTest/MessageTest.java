//package protobufTest;
//
//import cn.hutool.core.lang.Tuple;
//import com.google.protobuf.InvalidProtocolBufferException;
//import com.google.protobuf.Message;
//import com.google.protobuf.MessageOrBuilder;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.HashMap;
//
//public class MessageTest {
//    // 存放 msgId 和 msgBuilder 映射
//    static HashMap<Integer, Message.Builder> msgMap = new HashMap<>(); // 这里的 Message 要是 protoBuf 的 Message
//
//    public static void init(){
//        // 所有消息类型都放到 map 里
//        // TODO 尝试从配置文件读取
//        msgMap.put(1000, Welcome.newBuilder());
//        msgMap.put(1001, Talk.newBuilder());
//    }
//
//    // 从字节数组data 中读取到 msgId 和数据 body
//    public static Tuple readMessage(byte[] data){
//
//
//
//        int msgId = 0;
//        byte[] body = data;
//
//        Tuple tuple = new Tuple(msgId,body);
//        return tuple;
//    }
//
//    public static Message getMessageObject(byte[] data){
//        Message msg = null;
//        try {
//            msg = Talk.parseFrom(data); // 字节数组转化为 Message 对象
//        } catch (InvalidProtocolBufferException e) {
//            e.printStackTrace();
//        }
//        if(msg == null){
//            System.out.println("Get msg object failed!");
//            return msg;
//        }
//
//        //TODO
//        return msg;
//    }
//
//    public static void main(String[] args) {
//        // 创建消息对象
//        Welcome welcome =
//                Welcome.newBuilder()
//                .setGreeting(22)
//                .setContent("Hello!")
//                .build();
//
//        Talk talk =
//                Talk.newBuilder()
//                .setName("Jie")
//                .setContent("one two three")
//                .build();
//        // talk 对象转化为字节数组
//        byte[] talkByte = talk.toByteArray();   // 到这里就可以写到 netty 的 channel 或 buffer 里了
//
//
//        // 在 proto 中定义的 message 编译形成的类，都实现了MessageOrBuilder 接口
//        // 所以在不确定收到的消息类型时，可以用 MessageOrBuilder 来接收。
//        MessageOrBuilder talk1 = null;
//        try {
//            talk1 = Talk.parseFrom(talkByte); // 字节数组转化为 talk 对象
//        } catch (InvalidProtocolBufferException e) {
//            e.printStackTrace();
//        }
//
//        // welcome 对象转化为 outputStream 输出流
//        OutputStream out = new OutputStream() {
//            @Override
//            public void write(int b) throws IOException {
//            }
//        };
//        try {
//            welcome.writeTo(out);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // inputStream 流转化为 talk 对象
//        InputStream in = new InputStream(){
//            @Override
//            public int read() throws IOException {
//                return 0;
//            }
//        };
//        MessageOrBuilder talk2 = null;
//        try {
//            // 从字节数组中读取到 in 中
//            in.read();  // 因为没有重写 read，没有数据输入，所以 in 是空的
//            // talk2 = Talk.parseFrom(in);  // 这里的 talk2仍是 null. 因为 in 没有数据所以会报错
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
////        System.out.println(welcome);
////        System.out.println(talk);
//        System.out.println(talkByte);
//        if(talk1 != null) {
//            System.out.println("talk1: " +talk1);
//        }
//        System.out.println(out);
//        System.out.println("talk2: " + talk2);  // null
//    }
//}
