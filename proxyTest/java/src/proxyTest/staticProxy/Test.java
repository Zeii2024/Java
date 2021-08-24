package proxyTest.staticProxy;

public class Test {
    public static void main(String[] args) {
        // 创建目标对象
        IUser target = new RealUser();
        // 创建代理对象
        ProxyUser proxyUser = new ProxyUser(target);
        // 直接调用目标对象的方法
        System.out.println("target.getUserId: "+target.getUserId());
        // 通过代理调用目标对象的方法
        System.out.println("proxyUser.getUserId: "+proxyUser.getUserId());
    }
}
