package handlers;


import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import messages.Messages;

import java.util.HashMap;

public class MsgBuild {
    static final HashMap<Integer, Message.Builder> msgMap = new HashMap<Integer,Message.Builder>();// 这里的 Message 要是 protoBuf 的 Message

    static{
        // 所有消息类型都放到 map 里
        // TODO 尝试从配置文件读取
        msgMap.put(10000, Messages.Welcome.newBuilder());
        msgMap.put(10001, Messages.Talk.newBuilder());
    }

    public static Message build(int msgId, byte[] data) throws Exception {
         Message.Builder builder = msgMap.get(msgId);
         if(builder == null){
             throw new Exception("Builder NOT found!");
         }
         Message.Builder builder1 = builder.mergeFrom(data);

         return builder1.build();
     }

}
