package proxyTest.cjlibProxy;

// 目标类
public class RealUser {  // 不需要实现接口

    public int getUserId() {
        System.out.println("getUserId...");
        return 2562;
    }
}
