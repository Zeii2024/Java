package proxyTest.cjlibProxy;

public class Test {
    public static void main(String[] args) {
        RealUser target = new RealUser();
        // 输出下 target 的类
        System.out.println(target.getClass());

        ProxyFactory proxyFactory = new ProxyFactory(target);

        // 获取代理实例对象
        RealUser proxy = (RealUser) proxyFactory.getProxyInstance();

        //输出下 proxy 的类
        System.out.println(proxy.getClass());
        proxy.getUserId();
    }
}
