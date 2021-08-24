package proxyTest.dynamicProxy;

import java.lang.reflect.Proxy;

public class Test {
    public static void main(String[] args) {
        // 目标对象

        IUser target = new RealUser();
        // 获取 classLoader
        ClassLoader classLoader = target.getClass().getClassLoader();
        // 获取class 或 interface
//        Class [] classes = target.getClass().getInterfaces();  // 这两种写法都可以
        Class<?>[] classes = new Class[] {IUser.class}; // 也可以把接口的 class 放到数组里，但必须是接口
        // 获取 handler
        UserInvocationHandle handler = new UserInvocationHandle(target);

        // 通过动态代理获取代理对象实例
        IUser proxyInstance = (IUser) Proxy.newProxyInstance(classLoader, classes, handler);

        // 通过代理对象来调用方法
        System.out.println(proxyInstance.getUserId());

    }
}
