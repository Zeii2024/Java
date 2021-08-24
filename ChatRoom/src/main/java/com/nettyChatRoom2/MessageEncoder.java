package com.nettyChatRoom2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class MessageEncoder extends ChannelOutboundHandlerAdapter {
    private Object Exception;

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        Channel channel = ctx.channel();
        // 把 String编码为 ByteBuf
        // super.write(ctx, msg, promise);
        Message message = (Message) msg;
        int headLength = message.headCode.length();
        // 判断 headCode 的长度是否为4
        if(headLength != 4){
            throw new Exception(){
                @Override
                public void printStackTrace() {
                    super.printStackTrace();
                    System.out.println("The Length of HeadCode Should Be 4!");
                }
            };
        }
        int len = message.length;
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(headLength + 4 + len); // 长度
        // 采用零拷贝的 slice 方法，分别对消息头和消息体进行处理
        // 切片后的 ByteBuf 维护各自的读写指针，但共用原 ByteBuf 的内存区域
        ByteBuf headBuf = buf.slice(0,4);
        ByteBuf lengthBuf = buf.slice(5,4);
        ByteBuf bodyBuf = buf.slice(9,buf.capacity()-9);

        headBuf.writeBytes(message.headCode.getBytes());
        lengthBuf.writeByte(len);
        bodyBuf.writeBytes(message.msg.getBytes());

        msg = buf;
        System.out.println(msg);
        // 写入 channel
        channel.writeAndFlush(msg);

    }
}
