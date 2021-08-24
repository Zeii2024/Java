package handlers;


import cn.hutool.core.lang.Tuple;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import java.util.Iterator;
import java.util.Map;

public class ProtoHandler implements Handler<Buffer> {
    public ProtoHandler(){}

    public Tuple readBuffer(Buffer buffer){
        // 从 buffer 中读取 msgId 和 data，存放在 Tuple 中
        int allLength = buffer.getInt(0);
        int msgId = buffer.getInt(4);
        byte[] data = buffer.getBytes(4 + 4, allLength);

        return new Tuple(msgId,data);
    }

    // 打印收到的消息
    public void printMsg(int msgId, Message msg){
        System.out.println("Received msg: " + msgId + "\n" + msg);
    }

    public void writeBuffer(Buffer buffer, Message msg) throws Exception {
        // getOptions 是获取属性值
        Map<Descriptors.FieldDescriptor, Object> allFields = msg.getDescriptorForType().getOptions().getAllFields();
        // 利用迭代器获取 value，因为这里只有一个 Option值，所以拿到一个就 Ok
        Iterator<Descriptors.FieldDescriptor> iterator = allFields.keySet().iterator();
        int msgId = 0;
        while(iterator.hasNext()){
            Descriptors.FieldDescriptor next = iterator.next();
            msgId = (int) allFields.get(next);
        }
        if(msgId < 10000){
            throw new Exception("msgId is illegal!");
        }

        // 向 buffer 中写入数据
        write(buffer, msg, msgId);
    }

    public void write(Buffer buffer, Message msg, int msgId){
        byte[] data = msg.toByteArray();
        // Length:4字节 + msgId:4字节 + data
        int allLength = 4 + 4 + data.length;

        buffer.appendInt(allLength);
        buffer.appendInt(msgId);
        buffer.appendBytes(data);
    }

    @Override
    public void handle(Buffer buffer){
        // 由 msgId 和 data 构建出Message对象，并打印(或进行其他操作)
        Tuple tuple = readBuffer(buffer);

        Message msg = null;
        int msgId = tuple.get(0);
        try {
            msg = MsgBuild.build(msgId, tuple.get(1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        printMsg(msgId, msg);
    }
}
