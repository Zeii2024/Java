package protobufTest;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import messages.Messages;
import messages.Messages.Welcome;
import messages.Messages.Talk;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Test2 {
    static HashMap<Integer, Message.Builder> msgMap = new HashMap<Integer,Message.Builder>();

    static{
       // 所有消息类型都放到 map 里
       // TODO 尝试从配置文件读取
       msgMap.put(10000, Messages.Welcome.newBuilder());
       msgMap.put(10001, Messages.Talk.newBuilder());
    }

    public static void main(String[] args) {
        Welcome welcome = Welcome.newBuilder()
                .setGreeting(2021)
                .setContent("How are you")
                .build();

        // 获取属性 msgId
        Map<Descriptors.FieldDescriptor, Object> allFields = welcome.getDescriptorForType().getOptions().getAllFields();
        Iterator<Descriptors.FieldDescriptor> iterator = allFields.keySet().iterator();
        while (iterator.hasNext()){
            Descriptors.FieldDescriptor next = iterator.next();
            System.out.println(allFields.get(next));
        }
    }
}
