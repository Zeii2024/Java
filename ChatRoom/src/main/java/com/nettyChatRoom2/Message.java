package com.nettyChatRoom2;

// 定义 Message 协议
public class Message {
    public String headCode;
    public int length;

    public String msg;

    public Message(){};
    public Message(String headCode, String str){
        this.headCode = headCode;
        this.length = str.length();
        this.msg = str;
    };

}
