package com.nettyChatRoom2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;

public class MessageDecoder extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //对传入的 msg 对象进行解码，转换成 String 字符串
        Channel channel = ctx.channel();

        ByteBuf buf = (ByteBuf) msg;
        if(buf == null){
            System.out.println("message buf is NULL!");
        }
        // 判断头代码
        String headCode = buf.readBytes(buf.slice(0,4)).toString();
        if(!headCode.equals("aaaa")){
            System.out.println("Message is invalid");
        }
        // 获取长度
        int len = buf.readBytes(buf.slice(5,9)).getInt(0);
        //
        String body = buf.readBytes(buf.slice(10,10+len)).toString();

        msg = body;
        System.out.println("Decoder result:"+msg);
        channel.write(msg);

    }

}
